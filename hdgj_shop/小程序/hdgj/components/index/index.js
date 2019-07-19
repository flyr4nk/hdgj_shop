// components/index/index.js
Component({
  pageLifetimes: {
    show() {
      console.log("awqqeq");
      if (typeof this.getTabBar === 'function' &&
        this.getTabBar()) {
        this.getTabBar().setData({
          selected: 0
        })
      }
    }
  },
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    images: {
      url: 'http://img1.imgtn.bdimg.com/it/u=735014917,976731480&fm=26&gp=0.jpg',
    },
    menu:[
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' },
      { src: '/images/index/1.jpg', title: '港视港饮' }
    ],
    template_block:[
      {
        title:'每日精选',
        goods:[
          {
            url: '/images/index/1.jpg',
            title:'美国UASA保健品d大撒大撒敖德萨阿德撒旦阿德撒旦啊是大啊是大请问企鹅请问我去饿撒旦啊是大',
            explain:'好吃的好喝的好玩的',
            price:90.5,
            sellNum:54
          },
          {
            url: '/images/index/1.jpg',
            title: '美国UASA保健品',
            explain: '好吃的好喝的好玩的',
            price: 90.5,
            sellNum: 54
          },
          {
            url: '/images/index/1.jpg',
            title: '美国UASA保健品',
            explain: '好吃的好喝的好玩的',
            price: 90.5,
            sellNum: 54
          }
        ]
      },
      
       {
        title: '每日精选',
        goods: [
          {
            url: '/images/index/1.jpg',
            title: '美国UASA保健品d大撒大撒敖德萨阿德撒旦阿德撒旦啊是大啊是大请问企鹅请问我去饿撒旦啊是大',
            explain: '好吃的好喝的好玩的',
            price: 90.5,
            sellNum: 54
          },
          {
            url: '/images/index/1.jpg',
            title: '美国UASA保健品',
            explain: '好吃的好喝的好玩的',
            price: 90.5,
            sellNum: 54
          },
          {
            url: '/images/index/1.jpg',
            title: '美国UASA保健品',
            explain: '好吃的好喝的好玩的',
            price: 90.5,
            sellNum: 54
          }
        ]
      }
    ]
  },

  /**
   * 组件的方法列表
   */
  methods: {

  }
  
})
