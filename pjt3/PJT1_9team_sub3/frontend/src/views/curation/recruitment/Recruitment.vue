<template>
  <div id="container-box">
    <!-- 왼쪽 메뉴 부분 -->
    <div id="left-info">
      <!-- Curation -->
      <div id="type" class="b-title">
        <p class="m-b">Curation</p>
      </div>
      <!-- 해당 큐레이션 description -->
      <div id="description" class="rounded-bg container">
        <p class="b-desc m-b" @click="click">채용일정</p>
        <p class="l-desc">
          채용일정 너만아니<br />
          채용일정 나도알자
        </p>
      </div>
      <!-- 임박한 채용보기 -->
      <div id="due-date" class="rounded-bg container">
        <p class="b-desc m-b">임박한 채용일정</p>
        <DueDateItem
          v-for="(event, index) in deadlines"
          :key="event.id + '/' + index"
          :event="event"
          class="date-item"
        />
      </div>
    </div>
    <div id="center-post">
      <!-- 캘린더 들어가는 부분 -->
      <CalendarSpan id="study-calendar" class="rounded-bg" :boardName="'채용일정'" />

      <!-- 게시글 작성 -->
      <div class="create-post">
        <PostWrite :in-board="inBoard" style="margin:24px 10px 0" />
        <RecruitmentPostList />
      </div>
    </div>
    <CalendarDialog :boardName="'채용일정'" />
    <DetailDialog />
    <ModifyDialog :boardName="'채용일정'" />
  </div>
</template>
<script>
// 스터디 홍보글 게시물
// import Post from '@/components/board/Post.vue';
import DueDateItem from '@/views/curation/recruitment/DueDateItem.vue';
import CalendarSpan from '@/components/calendar/CalendarSpan';

// 중고장터 리스트
import RecruitmentPostList from '@/components/board/PostList';
//글작성
import PostWrite from '@/components/board/PostWrite';

export default {
  name: 'Recruitment',
  components: {
    // Post,
    DueDateItem,
    CalendarSpan,
    CalendarDialog: () => import('@/components/calendar/CalendarDialog'),
    DetailDialog: () => import('@/components/calendar/DetailDialog'),
    ModifyDialog: () => import('@/components/calendar/ModifyDialog'),

    PostWrite,
    RecruitmentPostList,
  },
  data() {
    return {
      inBoard: true,
    };
  },
  created() {
    this.$store.dispatch('calendar/actGetDeadline');
  },
  computed: {
    deadlines() {
      return this.$store.state.calendar.deadlines;
    },
  },
  methods: {
    click: function() {
      console.log('까꿍~');
    },
  },
};
</script>

<style scoped>
::-webkit-scrollbar-track {
  margin-top: 112px;
}

.m-b {
  margin-bottom: 0.6rem;
  font-weight: 700;
  font-size: 1.4rem;
}

.container {
  width: 100%;
  margin: 0px 0 20px;
  padding: 10px;
  box-shadow: var(--basic-shadow-w);
}
#container-box {
  position: absolute;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 0 auto;
  width: 100%;
  height: 100%;
  max-width: 1100px;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
}

/* 왼쪽 */
#left-info {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-right: 5px;
  padding: 0 15px;
  height: 100%;
  overflow-y: scroll;
}
/* 왼쪽 메뉴바 부분 */
#type {
  margin-top: 80px;
}
/* 스터디 보드 설명 */
#description {
}
.date-item {
  margin: 10px 0 0;
}

/* 오른쪽 */
#center-post {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 15px;
  flex: 2;
  height: 100%;
  overflow-y: scroll;
}
#study-calendar {
  margin-top: 116px;
  overflow: hidden;
  width: 100%;
  height: 650px;
  min-height: 650px;
  box-shadow: var(--basic-shadow-w);
}
/* 검색창 */
.center-post .search-bar {
  align-self: flex-end;
}
/* 글쓰기 및 리스트 */
.create-post {
  width: 100%;
}
/* 각각의 게시글들 */
.center-post .post-list {
  border: 2px dotted;
  margin: 2% 2%;
}
/* 모바일 웹화면 */
@media (max-width: 426px) {
  #container-box {
    position: relative;
    flex-direction: column;
  }
  #left-info {
    width: 100%;
    margin: 0;
    overflow-y: inherit;
  }
  #type {
    margin-top: 0;
  }
  #center-post {
    width: 100%;
    height: auto;
    overflow-y: inherit;
  }
  #study-calendar {
    margin-top: 0;
  }
}
</style>
