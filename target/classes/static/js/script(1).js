document.getElementById("messages").scrollTop = document.getElementById("messages").scrollHeight



function changeSize() {
    document.getElementById("window").style.width = window.innerWidth.toString() + "px"
    document.getElementById("window").style.height = window.innerHeight.toString() + "px"
}

changeSize();
window.onload = function () {
    changeSize();
    document.getElementById("messages").scrollTo(0, document.getElementById("messages").scrollHeight + 100);
  //  document.getElementById('msg-input').onfocus = focusOnInputBox;
  //  document.getElementById('channel_select').onfocus = focusOnChannelBox;
}
window.onresize = function () {
    changeSize();
}

