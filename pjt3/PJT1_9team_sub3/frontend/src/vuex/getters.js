export default {
  //userinfo
  getEmail: function(state) {
    return state.auth.user.email;
  },
  getToken: function(state) {
    return state.auth.user.token;
  },
  getUser: function(state) {
    return state.auth.user;
  },
  getDomain: function(state) {
    let email = state.auth.user.email;
    if (email == null) return null;
    let domain = email.substring(email.indexOf('@') + 1, email.length);
    return domain;
  },

  //searchState
  getSearchStateFilters: function(state) {
    return state.searchState.filters;
  },

  //spinner
  getSpinnerActive: function(state) {
    return state.spinnerActive;
  },
  getSpinnerMessage: function(state) {
    return state.spinnerMessage;
  },

  //Toast
  getToastActive: function(state) {
    return state.toastActive;
  },
  getToastType: function(state) {
    return state.toastType;
  },

  //problem, 랜덤으로 얻음
  getRandomProblem: function(state) {
    const randomValue = Math.floor(Math.random() * 5);
    return state.problems[randomValue];
  },
  getRandomProblem2: (state) => (beforeProblem) => {
    let randomValue;
    let getProblem;
    do {
      randomValue = Math.floor(Math.random() * 5);
      getProblem = state.problems[randomValue];
    } while (getProblem == beforeProblem);
    return getProblem;
  },
};
