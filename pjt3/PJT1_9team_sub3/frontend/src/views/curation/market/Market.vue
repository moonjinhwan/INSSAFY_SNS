<template>
  <v-app>
    <v-main class="main-bg-color">
      <v-container id="container" class="pt-8">
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
            <!-- 지역설정 -->
            <!-- <v-select
              v-model="searchLocation"
              :items="location"
              label="지역"
              solo
            ></v-select> -->
            <!-- 검색바 -->
            <!-- <v-text-field
              placeholder="검색"
              solo
              v-model="searchKeyword"
            ></v-text-field> -->
          </div>
        </v-row>
        <v-row dense>
          <!-- 왼쪽 중고장터 설명 부분 -->
          <v-col class="col-12 col-sm-4">
            <div id="description" class="rounded-bg container-description">
              <h4 class="b-desc">중고장터</h4>
              <p class="l-desc">
                내겐 필요없는 물건이<br />
                누군가에겐 꼭 필요하다면?
              </p>
              <v-divider class="my-2"></v-divider>
              <v-list color="transparent">
                <!-- 중고장터 설명 부분 -->
                <!-- 관심 품목 부분 -->
                <v-list-item
                  ><a id="scrap-item" v-b-toggle href="#item-collapse" @click.prevent
                    >관심품목 <b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
                ></v-list-item>
                <b-collapse visible id="item-collapse">
                  <v-col>
                    <MarketItem v-for="(likeId, idx) in likeIdList" :key="idx" :likeId="likeId" />
                  </v-col>
                </b-collapse>
              </v-list>
            </div>
          </v-col>
          <!-- 오른쪽 중고장터 본문 부분 -->
          <v-col class="col-12 col-sm-8">
            <PostWrite :in-board="inBoard" style="margin:0 10px" />
            <!-- 중고장터 게시물 부분 -->
            <MarketPostList />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
// 관심 품목 등록 리스트
import MarketItem from '@/components/curation/market/MarketItem.vue';

// 중고장터 리스트
import MarketPostList from '@/components/board/PostList';
//글작성
import PostWrite from '@/components/board/PostWrite';

import * as marketApi from '@/api/market';
import * as userApi from '@/api/user';

export default {
  name: 'Market',
  components: {
    MarketItem,
    // MarketPostWrite,
    MarketPostList,
    PostWrite,
    // MarketPost,
    // InfiniteLoading
  },
  // 뷰 인스턴스 제거될 때 resize 호출
  beforeDestroy() {
    if (typeof window === 'undefined') return;

    window.removeEventListener('resize', this.onResize, { passive: true });
  },
  // resize 실시해서 현재 화면 크기 확인
  mounted() {
    this.onResize();

    window.addEventListener('resize', this.onResize, { passive: true });
  },
  created() {
    this.getLikeItem();
  },
  data() {
    return {
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false,
        viewSize: 0,
      },
      // 검색 조건 지역 설정
      location: ['서울', '대전', '구미', '광주'],
      // 선택된 검색 조건 지역
      searchLocation: '',
      // 검색 키워드
      searchKeyword: '',
      posts: [],
      page: 0,
      inBoard: true,
      likeIdList: [],
    };
  },
  methods: {
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    getLikeItem() {
      userApi.getScraps(String(localStorage.userId)).then((res) => {
        res.data.scraps.forEach((e) => {
          if (e.board_name === '중고장터') this.likeIdList.push(e.post_id);
        });
      });
    },
  },
};
</script>

<style scoped>
/* 전체 감싼 컨테이너 */
#container {
  max-width: 1200px !important;
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
</style>
