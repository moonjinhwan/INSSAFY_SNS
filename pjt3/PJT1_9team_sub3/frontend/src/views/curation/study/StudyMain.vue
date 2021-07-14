<template>
  <v-app class="main-bg-color">
    <v-main class="basic-bg">
      <v-container class="pt-8">
        <!-- PC에서 보여줄 curation이름과 검색 -->
        <v-row
          v-if="!ResponsiveSize.isMobile"
          no-gutters
          dense
          class="d-flex 
            flex-row 
            justify-space-between"
        >
          <!-- 페이지 이름 -->
          <div class="text-overline  text-weight-black" style="font-size: 20px !important;">Curation</div>
          <!-- 검색관련 부분 -->
          <div
            class="d-flex 
              flex-row 
              justify-flex-end"
          >
            <!-- 검색바 -->
            <!-- <v-text-field
              placeholder="검색"
              solo
              v-model="searchKeyword"
            ></v-text-field> -->
          </div>
        </v-row>
        <v-row dense>
          <!-- 왼쪽 스터디 설명 부분 -->
          <v-col class="col-12 col-sm-4">
            <div id="description" class="rounded-bg container-description">
              <h4 class="b-desc">스터디</h4>
              <p class="l-desc">
                스터디원을 구해서<br />
                같이 공부해보는 건 어떨까요?
              </p>
              <v-divider class="my-2"></v-divider>
              <v-list color="transparent">
                <v-list-item
                  ><a id="scrap-item" v-b-toggle href="#item-collapse" @click.prevent
                    >내 스터디 목록 <b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
                ></v-list-item>
                <b-collapse visible id="item-collapse">
                  <v-col v-for="(group, idx) in myStudyGroup" :key="idx">
                    <MyStudyGroup :group="group" />
                  </v-col>
                </b-collapse>
              </v-list>
            </div>
          </v-col>
          <!-- 오른쪽 스터디 본문 부분 -->
          <v-col class="col-12 col-sm-8">
            <div class="main-header">
              <div class="flagPromoList">
                <v-switch
                  class="m-0"
                  v-model="flagPromoList"
                  inset
                  color="#0B2945"
                  :label="`${state}`"
                  @click="filterMyStudyGroup()"
                ></v-switch>
              </div>
              <div class="btnWrite" v-if="!flagPromoList"><PostWrite :in-board="inBoard" /></div>
              <div class="btnWrite" v-if="flagPromoList">
                <b-button class="btn-write" @click="goToCreateBoard">스터디 만들기</b-button>
              </div>
            </div>
            <!-- 스터디 게시글쓰기 -->
            <!-- <StudyPostWrite class="mx-4 mb-2"/> -->
            <!-- 스터디 게시물 부분 -->
            <StudyPromotionPostList v-if="!flagPromoList" />
            <AllGroupList v-if="flagPromoList" />

            <!-- <StudyPost class="mx-4 mb-2"/>
            <StudyPost class="mx-4 mb-2"/>
            <StudyPost class="mx-4 mb-2"/>  -->
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
// 스터디 홍보 게시물
// import StudyPost from "@/components/curation/study/StudyPost.vue"
// 스터디 홍보 게시물 쓰기
// import StudyPostWrite from "@/components/curation/study/StudyPostWrite.vue"
// 스터디 내 그룹
// import StudyGroup from "@/components/curation/study/StudyGroup.vue"
import MyStudyGroup from '@/components/curation/study/MyStudyGroup.vue';

import PostWrite from '@/components/board/PostWrite';
import StudyPromotionPostList from '@/components/board/PostList';
import AllGroupList from '@/components/curation/study/AllGroupList';

import * as studyApi from '@/api/study';

export default {
  name: 'StudyMain',
  components: {
    // StudyPost,
    // StudyPostWrite,
    // StudyGroup,
    MyStudyGroup,
    StudyPromotionPostList,
    AllGroupList,
    PostWrite,
  },
  // 뷰 인스턴스 제거될 때 resize 호출
  beforeDestroy() {
    if (typeof window === 'undefined') return;

    window.removeEventListener('resize', this.onResize, { passive: true });
  },
  created() {
    studyApi
      .getMyGroupList(localStorage.userId)
      .then((res) => {
        const group = res.data.studyList.filter((group) => {
          if (group != null) {
            return group;
          }
        });
        this.myStudyGroup = group;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  mounted() {
    // resize 실시해서 현재 화면 크기 확인
    this.onResize();

    window.addEventListener('resize', this.onResize, { passive: true });
    // this.filterMyStudyGroup()
  },
  data() {
    return {
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false,
        viewSize: 0,
      },
      // 검색 키워드
      searchKeyword: '',
      // 내 스터디 목록 활성화 버튼
      isMyStudy: false,
      myStudyGroup: {},
      state: '홍보 목록',
      flagPromoList: false,
      flagPromoGroup: ['스터디 목록', '홍보 목록'],
      inBoard: true,
    };
  },
  methods: {
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    // 내 스터디 그룹 / 전체 스터디 그룹 전환
    filterMyStudyGroup() {
      if (this.flagPromoList) {
        this.state = this.flagPromoGroup[0];
      } else {
        this.state = this.flagPromoGroup[1];
      }
    },
    goToCreateBoard() {
      return this.$router.push({ name: 'StudyGroupForm' });
    },
  },
};
</script>

<style scoped>
.basic-bg {
  background-color: var(--basic-color-bg);
}
.main-bg-color {
  background-color: #ebebe9;
}
.description {
  margin: 2%;
  padding: 10%;
  flex-basis: 20%;
}
.container-description {
  width: 100%;
  margin: 0px 0 20px;
  padding: 10px;
  box-shadow: var(--basic-shadow-w);
}

#scrap-item {
  text-decoration: none;
  color: #000;
}

/* 오른쪽 section*/
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 8px;
}
.flagPromoList {
  display: flex;
  align-items: center;
  /* margin-left:20px */
}
.v-messages {
  min-height: 0;
}
.btnWrite {
  /* width: 30%; */
}
.btn-write {
  position: sticky;
  text-align: center;
  margin: auto;
  height: 50px;
  width: 100%;
  border: none;
  color: var(--basic-color-fill);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: #ebebe9 !important;
  box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff;
  border-radius: 15px !important;
  transition: 0.3s all ease;
}
.btn-write:hover,
.btn-write:active,
.btn-write:focus {
  color: #ebebe9 !important;
  background-color: var(--basic-color-key) !important;
}
</style>
