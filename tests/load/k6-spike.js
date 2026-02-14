import http from 'k6/http'
import { check, sleep } from 'k6'

export const options = {
  stages: [
    { duration: '5s', target: 5 },
    { duration: '10s', target: 30 },
    { duration: '5s', target: 0 }
  ],
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<800']
  }
}

const BASE = __ENV.BASE_URL || 'http://localhost:8080'

export default function () {
  const res = http.get(`${BASE}/api/products`)
  check(res, {
    'status is 200': (r) => r.status === 200
  })
  sleep(0.2)
}
