import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';

const TEST_TYPE = __ENV.TEST_TYPE || 'smoke';
const BASE_URL = __ENV.BASE_URL || 'http://host.docker.internal:8080';
const ORDER_ENDPOINT = '/api/v1/orders';
const PRODUCT_ID = 4; // 테스트용 상품 ID

const MOCK_USER_START_ID = 1; 
const MOCK_USER_END_ID = 30;

const successRate = new Rate('success_rate');
const failCount = new Counter('fail_count');
const requestDuration = new Trend('request_duration');

// Mock 사용자 ID 생성 함수
function generateMockUserId() {
    return Math.floor(Math.random() * (MOCK_USER_END_ID - MOCK_USER_START_ID + 1)) + MOCK_USER_START_ID;
}

export let options = {};

switch (TEST_TYPE) {
    case 'smoke':
        options = {
            vus: 100,
            duration: '10s',
        };
        break;

    case 'load':
        options = {
            stages: [
                { duration: '30s', target: 2000 },
                { duration: '1m', target: 2000 },
                { duration: '30s', target: 0 },
            ],
            thresholds: {
                http_req_duration: ['p(95)<1000'],
                http_req_failed: ['rate<0.15'],
                'success_rate': ['rate>0.85'],
            },
        };
        break;

    case 'stress':
        options = {
            stages: [
                { duration: '1m', target: 1000 },
                { duration: '1m', target: 1000 },
                { duration: '1m', target: 2000 },
                { duration: '1m', target: 2000 },
                { duration: '1m', target: 3000 },
                { duration: '1m', target: 3000 },
                { duration: '1m', target: 4000 },
                { duration: '1m', target: 4000 },
                { duration: '1m', target: 5000 },
                { duration: '1m', target: 5000 },
                { duration: '30s', target: 0 },
            ],
            thresholds: {
                http_req_duration: ['p(95)<1000'],
                http_req_failed: ['rate<0.15'],
                'success_rate': ['rate>0.85'],
            },
        };
        break;

    case 'peak':
        options = {
            stages: [
                { duration: '10s', target: 100 },
                { duration: '10s', target: 100 },
                { duration: '20s', target: 2000 },
                { duration: '10s', target: 100 },
                { duration: '20s', target: 0 },
            ],
            thresholds: {
                http_req_duration: ['p(95)<1000'],
                http_req_failed: ['rate<0.15'],
                'success_rate': ['rate>0.85'],
            },
        };
        break;

    default:
        console.error(`Unknown TEST_TYPE: ${TEST_TYPE}`);
        break;
}

export default function () {
    const userId = generateMockUserId(); // Mock 사용자 ID 사용
    const url = `${BASE_URL}${ORDER_ENDPOINT}`;
    
    // 주문 요청 페이로드 - 각 주문당 5개씩 주문
    const payload = JSON.stringify({
        "userId": userId,
        "userCouponId": null,
        "orderProducts": [
            {
                "productId": PRODUCT_ID,
                "quantity": 5
            }
        ]
    });

    const headers = { 'Content-Type': 'application/json' };
    const res = http.post(url, payload, { headers });

    const success = check(res, {
        'status is 200': (r) => r.status === 200,
        'total request': (r) => [200, 409, 400].includes(r.status), // 409: 재고 부족, 400: 잘못된 요청
    });

    successRate.add(success);
    if (!success) failCount.add(1);
    requestDuration.add(res.timings.duration);

    sleep(1);
}
