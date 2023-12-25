//引入mockjs
import Mock from 'mockjs'
//使用mockjs模拟数据
// Mock.mock('http://localhost:8088/user/Navigation/HomePage', {
//     "ret":0,
//     "data|4":
//         [{
//             // "mtime": "@datetime",//随机生成日期时间
//             // "score|1-800": 1,//随机生成1-800的数字
//             // "rank|1-100": 1,//随机生成1-100的数字
//             // "stars|1-5": 1,//随机生成1-5的数字
//             // "nickname": "@cname",//随机生成中文名字
//             // //生成图片
//             // "img":"@image('200x100', '#ffcc33', '#FFF', 'png', 'Fast Mock')"
//             "number":"@natural(1, 100)",
//             // "username":"@cname",
//             // "password":"@string(6, 10)"
//         }]
// });
Mock.mock('http://localhost:8088/user/Navigation/HomePage', 'get', function() {
    let data = [];
  for(let i = 0; i < 3; i++) {
    data.push(Mock.Random.natural(1, 100));
  }
  return data;
  });

  Mock.mock('http://localhost:8088/user/Navigation/myDevice/devInformation', {
    "ret":0,
    "data|12-30":
        [{
            // "mtime": "@datetime",//随机生成日期时间
            // "score|1-800": 1,//随机生成1-800的数字
            // "rank|1-100": 1,//随机生成1-100的数字
            // "stars|1-5": 1,//随机生成1-5的数字
            "ID": "@natural(1,100)",
            "name": "@string(6, 10)",
            "type": "@string(6, 10)",
            // //生成图片
            // "img":"@image('200x100', '#ffcc33', '#FFF', 'png', 'Fast Mock')"
            "state":"正常 | 告警",//随机生成布尔值
            // "username":"@cname",
            // "password":"@string(6, 10)"
        }]
});

Mock.mock('http://localhost:8088/user/Navigation/dataQuery', {
  "ret":0,
  "data|3-30":
      [{
          // "mtime": "@datetime",//随机生成日期时间
          // "score|1-800": 1,//随机生成1-800的数字
          // "rank|1-100": 1,//随机生成1-100的数字
          // "stars|1-5": 1,//随机生成1-5的数字
          "ID": "@natural(1,100)",
          "name": "@string(6, 10)",
          "type": "@string(6, 10)",
          // //生成图片
          // "img":"@image('200x100', '#ffcc33', '#FFF', 'png', 'Fast Mock')"
          "time":"@datetime",
          "data":"@string(6, 10)",
          // "username":"@cname",
          // "password":"@string(6, 10)"
      }]
});

// Mock.mock('http://localhost:8088/login', 'get', (options) => {
//   // 从 URL 中解析出查询参数
//   const query = options.url.split('?')[1]
//   const params = new URLSearchParams(query)

//   // 获取具体的查询参数
//   const id = params.get('id')

//   // 根据查询参数返回相应的数据
//   if (id === '1') {
//     return Mock.mock({
//       'name': '张三',
//       'age|1-100': 50,
//       'color': '@color'
//     })
//   } else {
//     return Mock.mock({
//       'name': '李四',
//       'age|1-100': 50,
//       'color': '@color'
//     })
//   }
// })
Mock.mock(RegExp('http://localhost:8088/login/*'), {
    "success":1,
    "token":"@string(6, 10)",
});

Mock.mock('http://localhost:8088/signup', 'post', (options) => {
    return Mock.mock({
      "success":1,
      "token":"@string(6, 10)",
    })

})