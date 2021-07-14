export default {
  //spiner 토글
  setSpinnerTogle(state) {
    state.spinnerActive = !state.spinnerActive;
  },
  setSpinnerMessage(state, message) {
    state.spinnerMessage = message;
  },

  //Toast
  setToastTogle(state) {
    state.toastActive = !state.toastActive;
  },
  setToastClose(state) {
    state.toastActive = false;
  },
  setToastType(state, typeName) {
    if (typeName == 'search') {
      state.toastType = INIT_TOAST_TYPE();
      state.toastType.search = true;
    } else if (typeName == 'myinfo') {
      state.toastType = INIT_TOAST_TYPE();
      state.toastType.myinfo = true;
    } else if (typeName == 'email') {
      state.toastType = INIT_TOAST_TYPE();
      state.toastType.email = true;
    }
  },

  //comment
  CREATE_COMMENT(state, commentItem) {
    state.comments.push(commentItem);
  },

  //post
  CREATE_POST(state, postItem) {
    state.posts.push(postItem);
  },
};

const INIT_TOAST_TYPE = () => {
  return {
    search: false,
    myinfo: false,
    email: false,
  };
};
