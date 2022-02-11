var E = window.wangEditor;
// 创建editor
var editor = new E('#editor');
editor.customConfig.uploadImgServer = '/upload';
// 隐藏插入网络图片的功能
editor.customConfig.showLinkImg = false;
editor.customConfig.uploadFileName = 'file';
editor.create();