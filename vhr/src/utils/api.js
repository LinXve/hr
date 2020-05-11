import axios from 'axios'
import {Message} from 'element-ui'


// 请求拦截
axios.interceptors.request.use(config=>{
  // 每次发送请求前判断vuex中是否存在token
  // if 存在，则统一在http请求的header都加上token，这样后台判断你的登录清况
  // const token=store.state.token;
  // token && (config.headers.Authorization = token);
  return config;
},err=>{
  Message.error({message: '请求超时！'});
  return Promise.resolve(err);
})
// 响应拦截
axios.interceptors.response.use(data=>{
  if(data.status && data.status== 200 && data.data.status == 500){
    Message.error({message: data.data.msg});
    return;
    }
    if(data.data.msg){
      Message.success({message: data.data.msg});
    }
    return data;
}, err=> {
  if(err.response.status == 504 || err.response.status == 404){
    Message.error({message: '服务器被吃了！'});
  }else if(err.response.status == 403){
    Message.error({message: '权限不足！'});
  }else if(err.response.status == 500){
    Message.error({message: err.response.data.msg});
  }else{
    if(err.response.data.msg){
      Message.error({message: err.response.data.msg});
    }else{
      Message.error({message: '未知错误!'});
    }
  }
})

let base= '';

export const postRequest = (url,params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function(data){
      let ret='';
      for (let it in data) {
        ret+=encodeURIComponent(it) + '=' +encodeURIComponent(data[it]) +'&'
      }
      return ret;
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  });
}

export const uploadFileRequest =(url,params) =>{
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function(data){
      let ret= ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' +encodeURIComponent(data[it]) +'&'
      }
      return;
    }],
    headers: {
       'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  });
}

export const putRequest = (url,params)=>{
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret= ''
      for (let it in params) {
        ret+= encodeURIComponent(it) + '=' + encodeURIComponent(data[it])+ '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}

export const deleteRequest = (url,params) => {
  return axios({
    method: 'delete',
    url: `${base}${url}`,
   data: params,
   transformRequest: [function(data){
     let ret= ''
     for (let it in data) {
       ret += encodeURIComponent(it) + '=' +encodeURIComponent(data[it]) +'&'
     }
     return;
   }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  });
}

export const getRequest =(url,params)=>{
  return axios({
    method: 'get',
    url: `${base}${url}`,
   data: params,
   transformRequest: [function(data){
     let ret= ''
     for (let it in data) {
       ret += encodeURIComponent(it) + '=' +encodeURIComponent(data[it]) +'&'
     }
     return;
   }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}
