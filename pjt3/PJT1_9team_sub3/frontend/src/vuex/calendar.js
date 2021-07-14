import * as calendarApi from '@/api/calendar';

const INIT_EVENT = () => {
  return {
    name: '',
    content: '',
    startDate: '',
    // startTime: '',
    endDate: '',
    // endTime: '',
    hasTime: false,
  };
};

export default {
  namespaced: true,
  state: {
    event: INIT_EVENT(),
    events: [],
    dialog: false,
    detailDialog: false,
    modifyDialog: false,
    deadlines: [],
  },
  mutations: {
    //서버에서 응답한 보드에 관련된 일정 리스트를 vuex에 설정
    SET_CALENDAR_LIST(state, payload) {
      state.events = [...payload];
    },

    //dialog 열릴 때, 사용되는 변수(state) 초기화
    OPEN_CALENDAR_DIALOG(state, payload) {
      state.event.startDate = payload.date;
      state.event.startTime = payload.time;
      state.event.hasTime = false;
      state.dialog = true;
    },
    //dialog 닫기
    CLOSE_CALENDAR_DIALOG(state) {
      state.dialog = false;
      state.event = INIT_EVENT();
    },

    //detail dialog 띄우기
    OPEN_EVENT_DETAIL_DIALOG(state, payload) {
      state.event = payload;
      state.detailDialog = true;
    },
    CLOSE_EVENT_DETAIL_DIALOG(state) {
      state.detailDialog = false;
      state.event = INIT_EVENT();
    },

    //modify dialog 띄우기
    OPEN_MODIFY_CALENDAR_DIALOG(state, event) {
      state.detailDialog = false;
      state.event = event;
      state.modifyDialog = true;
    },
    CLOSE_MODIFY_CALENDAR_DIALOG(state) {
      state.modifyDialog = false;
      state.event = INIT_EVENT();
    },

    //임박한 일정 값 설정
    SET_DEADLINE_LIST(state, payload) {
      state.deadlines = payload;
    },

    //일정 삭제시 vuex events 리스트에서 해당 보드 제거
    DELETE_EVENT(state, id) {
      const index = state.events.findIndex((event) => event.id === id);
      state.events.splice(index, 1);
      state.detailDialog = false;
      state.event = INIT_EVENT();
    },

    //vuex로 일정 관리
    MODIFY_EVENT(state, payload) {
      const index = state.events.findIndex((event) => event.id === payload.id);
      let event = state.events.splice(index, 1);
      event = payload;
      state.events.push(event);
      state.modifyDialog = false;
      state.event = INIT_EVENT();
    },
  },

  actions: {
    //캘린더 리스트 요청
    async actGetEvents({ commit }, boardName) {
      try {
        const response = await calendarApi.getCalendar(getBoardId(boardName));
        if (response.data.message === 'SUCCESS') {
          // console.log(response.data.calendars);
          let payload = getEventsTransfer(response.data.calendars);
          commit('SET_CALENDAR_LIST', payload);
        }
      } catch (error) {
        console.log(error);
        alert('일정 목록을 가져오는 도중 문제가 발생했습니다.');
      }
    },

    //캘린더 일정 등록
    async actAddEvent(context, payload) {
      try {
        //db 컬럼에 맞는 데이터로 변환
        const board_id = getBoardId(payload.boardName);
        const apiEvent = makeApiEvent(board_id, payload.event);
        const response = await calendarApi.postCalendarCreate(apiEvent);
        if (response.data.message === 'SUCCESS') {
          context.dispatch('actGetEvents', payload.boardName);
          context.dispatch('actGetDeadline');
          context.commit('CLOSE_CALENDAR_DIALOG');
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('일정 등록 중에 문제가 발생했습니다.');
      }
      return false;
    },

    //임박한 일정 리스트 요청(마감일 7일 내 일정)
    async actGetDeadline({ commit }) {
      try {
        const response = await calendarApi.getDeadline();
        // console.log(response);
        if (response.data.message === 'SUCCESS') {
          const payload = getEventsTransfer(response.data.calendarList);
          commit('SET_DEADLINE_LIST', payload);
        }
      } catch (error) {
        console.log(error);
        alert('임박한 채용 리스트를 불러오는 중에 문제가 발생했습니다.');
      }
    },

    //일정 삭제
    async actDeleteEvent(context, event_id) {
      try {
        const response = await calendarApi.putCalendarDelete(event_id);
        if (response.data.message === 'SUCCESS') {
          context.commit('DELETE_EVENT', event_id);
          context.dispatch('actGetDeadline');
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('일정 삭제 중 문제가 발생했습니다.');
      }
      return false;
    },

    //일정 수정
    async actModifyEvent(context, payload) {
      try {
        const board_id = getBoardId(payload.boardName);
        const apiEvent = makeApiEvent(board_id, payload.event);
        const response = await calendarApi.putCalendarUpdate(apiEvent);
        if (response.data.message === 'SUCCESS') {
          //events 배열 값 수정
          console.log(payload);
          const newEvent = makeEvent(payload.event);
          console.log(newEvent);
          context.commit('MODIFY_EVENT', newEvent);
          context.dispatch('actGetDeadline');
          return true;
        }
      } catch (error) {
        console.log(error);
        alert('일정 수정 중에 문제가 발생했습니다.');
      }
      return false;
    },
  },

  getters: {
    //calendar id로 event 찾기
    getEventById: (state) => (id) => {
      return state.events.find((event) => event.id === id);
    },

    //component에서 calendar 요청할 때, 시간 분할
    getEventToVue(state) {
      return makeEventsToVue(state.event);
    },
  },
};

/*
//기타 기능 메서드
1. 보드 이름에서 보드 아이디 변환
2. 컬러 배열
3. 일정 등록에 따른 시간 변환
4. vuex 이벤트 생성
5. rest api 이벤트 생성
6. rest api로 가져온 일정 리스트에서 시간 설정 변환(시작, 끝 시간 같으면 시간 제거)
7. rest api로 가져온 데이터 vuex 데이터로 전환
8. Vue에서 요청할 때 time 분리
*/

//1
const getBoardId = (type) => {
  switch (type) {
    case '채용일정':
      return 123;
    default:
      return type;
  }
};
//2
const colors = [
  '#70ae98',
  '#ecbe7a',
  '#e58b88',
  '#9dabdd',
  '#bc85a3',
  '#909090',
  '#34568B',
  '#88B04B',
  '#F7CAC9',
  '#955251',
  '#009B77',
  '#B565A7',
  '#DFCFBE',
  '#282D3C',
  '#944743',
  '#7F4145',
  '#766F57',
];
//3
const getTime = (hasTime, time) => {
  return hasTime ? ` ${time}:00` : '';
};
//4. vuex 이벤트 생성(일정 수정 후 리스트 update)
const makeEvent = (event) => {
  return {
    id: event.id,
    name: event.name,
    content: event.content,
    start: event.startDate + getTime(event.hasTime, event.startTime),
    startDate: event.startDate,
    startTime: event.startTime,
    end: event.endDate + getTime(event.hasTime, event.endTime),
    endDate: event.endDate,
    endTime: event.endTime,
    hasTime: event.hasTime,
    color: colors[Math.floor(Math.random() * 17)],
  };
};
//5
const makeApiEvent = (board_id, event) => {
  return {
    board_id: board_id,
    calendar_item_id: event.id,
    calendar_item_title: event.name,
    calendar_item_description: event.content,
    calendar_item_datetime_start: `${event.startDate}${getTime(event.hasTime, event.startTime)}`,
    calendar_item_datetime_end: `${event.endDate}${getTime(event.hasTime, event.endTime)}`,
  };
};
//6
const timeChecker = (start, end) => {
  const starts = start.split(' ');
  const ends = end.split(' ');
  if (starts[1] == ends[1]) {
    return {
      start: starts[0],
      end: ends[0],
    };
  }
  return false;
};
//7
const getEventsTransfer = (arr) => {
  let payload = [];
  arr.forEach((el) => {
    let transfer = {
      id: el.calendar_item_id,
      name: el.calendar_item_title,
      content: el.calendar_item_description,
      start: el.calendar_item_datetime_start,
      startDate: el.calendar_item_datetime_start.slice(0, 10),
      end: el.calendar_item_datetime_end,
      endDate: el.calendar_item_datetime_end.slice(0, 10),
      color: colors[Math.floor(Math.random() * 17)],
    };
    const check = timeChecker(transfer.start, transfer.end);
    if (check) {
      transfer.start = check.start;
      transfer.end = check.end;
    }
    payload.push(transfer);
  });
  return payload;
};
//8
const makeEventsToVue = (event) => {
  if (event.name == '' || event.start == undefined) return event;
  let makeEvent = { ...event };
  const starts = event.start.split(' ');
  const ends = event.end.split(' ');
  makeEvent.startDate = starts[0];
  makeEvent.endDate = ends[0];
  if (starts[1] == undefined) {
    makeEvent.hasTime = false;
    return makeEvent;
  }
  if (starts[1] !== ends[1]) {
    makeEvent.startTime = starts[1];
    makeEvent.endTime = ends[1];
    makeEvent.hasTime = true;
  }
  return makeEvent;
};
