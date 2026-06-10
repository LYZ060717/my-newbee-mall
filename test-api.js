/**
 * 新蜂商城 API 测试脚本
 * 用法：node test-api.js
 */

const http = require('http');
const crypto = require('crypto');

const BASE_URL = '172.21.3.8';
const PORT = 28019;

// ========== 工具函数 ==========

function request(method, path, body = null, token = null) {
  return new Promise((resolve, reject) => {
    const options = {
      hostname: BASE_URL,
      port: PORT,
      path: path,
      method: method,
      headers: { 'Content-Type': 'application/json' },
      timeout: 10000
    };

    if (token) options.headers['token'] = token;

    const data = body ? JSON.stringify(body) : null;
    if (data) options.headers['Content-Length'] = Buffer.byteLength(data);

    const req = http.request(options, res => {
      let chunks = [];
      res.on('data', c => chunks.push(c));
      res.on('end', () => {
        const raw = Buffer.concat(chunks).toString();
        try { resolve({ status: res.statusCode, data: JSON.parse(raw) }); }
        catch { resolve({ status: res.statusCode, data: raw }); }
      });
    });

    req.on('error', reject);
    req.on('timeout', () => { req.destroy(); reject(new Error('请求超时')); });
    if (data) req.write(data);
    req.end();
  });
}

function md5(str) {
  return crypto.createHash('md5').update(str).digest('hex');
}

// ========== 测试用例 ==========

async function testLogin() {
  console.log('\n========== 1. 测试登录 ==========');
  const res = await request('POST', '/mallapi/api/v1/user/login', {
    loginName: '13800138000',
    passwordMd5: md5('123456')
  });
  console.log('状态码:', res.status);
  console.log('响应:', JSON.stringify(res.data, null, 2));

  if (res.data.resultCode === 200) {
    console.log('✅ 登录成功，Token:', res.data.data);
    return res.data.data;
  } else {
    console.log('❌ 登录失败:', res.data.message);
    return null;
  }
}

async function testGetUserInfo(token) {
  console.log('\n========== 2. 测试获取用户信息 ==========');
  const res = await request('GET', '/mallapi/api/v1/user/info', null, token);
  console.log('状态码:', res.status);
  console.log('响应:', JSON.stringify(res.data, null, 2));
  console.log(res.data.resultCode === 200 ? '✅ 成功' : '❌ 失败');
}

async function testIndex() {
  console.log('\n========== 3. 测试首页数据 ==========');
  const res = await request('GET', '/mallapi/api/v1/index-infos');
  console.log('状态码:', res.status);
  if (res.data.resultCode === 200) {
    const d = res.data.data;
    console.log('✅ 成功 | 轮播图:', d.carousels?.length, '| 热销:', d.hotGoodses?.length, '| 新品:', d.newGoodses?.length, '| 推荐:', d.recommendGoodses?.length);
  } else {
    console.log('❌ 失败:', res.data.message);
  }
}

async function testSearch(token) {
  console.log('\n========== 4. 测试商品搜索 ==========');
  const res = await request('GET', encodeURI('/mallapi/api/v1/search?keyword=手机&page=1'), null, token);
  console.log('状态码:', res.status);
  if (res.data.resultCode === 200) {
    console.log('✅ 成功，共', res.data.data?.list?.length, '条结果');
  } else {
    console.log('❌ 失败:', res.data.message);
  }
}

async function testCategories() {
  console.log('\n========== 5. 测试分类数据 ==========');
  const res = await request('GET', '/mallapi/api/v1/categories');
  console.log('状态码:', res.status);
  if (res.data.resultCode === 200) {
    console.log('✅ 成功，共', res.data.data?.length, '个分类');
  } else {
    console.log('❌ 失败:', res.data.message);
  }
}

// ========== 主流程 ==========

async function main() {
  console.log('🚀 新蜂商城 API 测试开始\n');
  console.log('目标服务器:', `http://${BASE_URL}:${PORT}`);

  try {
    // 1. 登录
    const token = await testLogin();

    // 2. 带 Token 的接口
    if (token) {
      await testGetUserInfo(token);
    }

    // 3. 首页和分类（不需要 Token）
    await testIndex();
    await testCategories();

    // 4. 搜索（需要 Token）
    if (token) {
      await testSearch(token);
    }

    console.log('\n🎉 测试完成！');
  } catch (e) {
    console.error('\n💥 测试出错:', e.message);
  }
}

main();
