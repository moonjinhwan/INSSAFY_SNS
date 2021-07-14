export default {
  namespaced: true,
  state: {
    // 현재 선택된 채팅방
    selectedChatRoom: '',
    // 현재 선택된 채팅방 아이디
    selectedId: '',
    // 현재 선택된 채팅방 상대 닉네임
    selectedNickname: '',
    // 가져올 채팅 메세지들
    selectedMessages: [],
    // 채팅방 출입 탐지 flag
    isInChatRoom: false,
    // 채팅방에서 메세지 보내면 갱신해줘야 함(채팅 리스트의 최근 메세지)
    isSend: true,
    // socket연결 확인 flag
    stompClient: null,
    // socket 통신으로 받은 메세지
    socketMsg: '',
    // 채팅방 1 => 2로 이동할 때 flag
    isSwitch: false,

  },
  getters: {

  },
  mutations: {
    // response에서 받아오는 부분
    IS_SELECTED(state, data) {
      state.selectedChatRoom = data.roomId
      state.selectedNickname = data.opp_nickName
      state.selectedId = data.opp_id
      state.isInChatRoom = !state.isInChatRoom
    },
    // 채팅방 선택 안되면 초기화
    IS_NOT_SELECTED(state) {
      state.selectedChatRoom = ''
      state.selectedNickname = ''
      state.selectedId = ''
      state.isInChatRoom = false
      state.selectedMessages = []
    },
    // 메세지 가져와서 저장
    GET_MESSAGES(state, msgList) {
      state.selectedMessages = msgList
    },
    // 메세지 입력하면 입력했다고 이벤트 발생
    SEND_MESSAGE(state) {
      state.isSend = !state.isSend
    },
    // socket 연결 시 확인 후 입력
    CHECK_SOCKET(state, stomp) {
      state.stompClient = stomp
    },
    // socket 연결로 받은 메세지
    PUSH_MSG(state, subscribe) {
      state.socketMsg = subscribe
    },
    // 채팅방 간 이동탐지
    SWITCH_ROOM(state) {
      state.isSwitch = !state.isSwitch
    }
  },
  actions: {
    isSelected({commit}, chatList) {
      commit('IS_SELECTED', chatList)
    },
    isNotSelected({commit}) {
      commit('IS_NOT_SELECTED')
    },
    // 채팅방 내 메세지들 가져오는 부분
    getMessages({commit}, msgList) {
      commit('GET_MESSAGES', msgList)
    },
    sendMessages({commit}) {
      commit('SEND_MESSAGE')
    },
    // 소켓 상태변환
    checkSocket({commit}, stomp) {
      commit('CHECK_SOCKET', stomp)
    },
    // 소켓으로 받은 메세지
    pushMsg({commit}, subscribe) {
      commit('PUSH_MSG', subscribe)
    },
    // 채팅방 간 이동
    switchRoom({commit}) {
      commit('SWITCH_ROOM')
    },
  },
}