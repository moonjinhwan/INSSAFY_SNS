<!--
  main 페이지 vue
  swiper.js 사용
 -->
<template>
  <div id="main-container">
    <div id="favorite-container" class="m-top-s">
      <p id="" class="main-title pc-only">FAVORITE</p>
      <svg
        id="f-star"
        class="mobile-only"
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
      >
        <path
          d="M12 .587l3.668 7.568 8.332 1.151-6.064 5.828 1.48 8.279-7.416-3.967-7.417 3.967 1.481-8.279-6.064-5.828 8.332-1.151z"
        />
      </svg>
      <swiper v-cloak id="f-swiper" class="swiper" :options="swiperOption" @clickSlide="clickFavorite">
        <swiper-slide v-for="(item, index) in favorites" :key="`faborite${item.board_id}/${index}`">
          <Slide :favorite="item" v-if="!item.type" />
          <SlideRecom :favorite="item" v-if="item.type" />
        </swiper-slide>
        <div class="swiper-pagination" slot="pagination"></div>
      </swiper>
    </div>
    <div id="curation-container">
      <p id="c-title" class="b-title">Curation</p>
      <div id="c-list" class="l-desc">
        <button class="c-btn newmorphism" @click="clickCBtn1">스터디</button>
        <button class="c-btn newmorphism" @click="clickCBtn2">학습공유</button>
        <button class="c-btn newmorphism" @click="clickCBtn3">채용일정</button>
        <button class="c-btn newmorphism" @click="clickCBtn4">중고장터</button>
        <button class="c-btn newmorphism" @click="clickCBtn5">대나무숲</button>
      </div>
    </div>
    <div id="popular-container" class="m-top">
      <div id="p-header">
        <p class="main-title">POPULAR</p>
        <div class="container-m-r">
          <button class="p-button r-desc" @click="clickPBtn1">보드 리스트</button>
          <button class="p-button r-desc" @click="clickPBtn2">게시글 리스트</button>
        </div>
      </div>
      <p class="p-desc l-desc">구독자 수</p>
      <div class="p-item-container">
        <BoardItem
          class="p-item"
          v-for="item in getFollowRank"
          :key="`p-subs${item.board_id}`"
          :item="item"
          :type="'subscribe'"
        />
      </div>
      <p class="p-desc l-desc">게시글 수</p>
      <div class="p-item-container">
        <BoardItem
          class="p-item"
          v-for="item in getPostsRank"
          :key="`p-posts${item.board_id}`"
          :item="item"
          :type="'post'"
        />
      </div>
      <div id="sub-title-container">
        <p class="p-desc l-desc">좋아요 수</p>
        <p class="p-desc l-desc" style="margin-left: 2%">댓글 수</p>
      </div>
      <div id="p-item-container2">
        <p class="p-desc l-desc mobile">좋아요 수</p>
        <PostItem v-cloak class="p-item2" :items="getLikeRank" :key="componentKey + 'like'" :type="`like`" />
        <p class="p-desc l-desc mobile">댓글 수</p>
        <PostItem v-cloak class="p-item2" :items="getCommentRank" :key="componentKey + 'comment'" :type="`comment`" />
      </div>
    </div>
  </div>
</template>

<script>
// import "../../components/css/user.scss";
// import * as authApi from '@/api/auth';
//https://github.surmon.me/vue-awesome-swiper/?ref=madewithvuejs.com
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
import 'swiper/css/swiper.css';
import BoardItem from '@/components/popular/BoardItem.vue';
import PostItem from '@/components/popular/PostItem.vue';
import { mapGetters, mapActions } from 'vuex';

//toast nority

export default {
  name: 'Main',
  components: {
    //slide
    Slide: () => import('@/components/favorite/Slide'),
    SlideRecom: () => import('@/components/favorite/SlideRecom'),
    //vue-awesome-swiper
    Swiper,
    SwiperSlide,
    //popular
    BoardItem,
    PostItem,
    //
  },
  data() {
    return {
      componentKey: 0,
      favorites: [],
      options: {
        width: '400px',
        position: 'bottom-right',
        padding: '1rem',
      },
      swiperOption: {
        effect: 'coverflow',
        grabCursor: true,
        // centeredSlides: true,
        // speed: 300,
        slidesPerView: 5,
        freeMode: true,
        initialSlide: 1,
        coverflowEffect: {
          rotate: 5,
          stretch: -5,
          depth: 50,
          modifier: 1,
          slideShadows: true,
        },
        // navigation: {
        //   nextEl: '.swiper-button-next',
        //   prevEl: '.swiper-button-prev',
        // },
        pagination: {
          el: '.swiper-pagination',
          clickable: true,
        },
        breakpoints: {
          //pc big size
          1536: {
            slidesPerView: 7,
          },
          //pc small size
          769: {
            slidesPerView: 5,
          },
          //tablet
          426: {
            slidesPerView: 3,
          },
          //mobile
          320: {
            slidesPerView: 2,
          },
        },
      },
    };
  },
  created() {
    if (this.$store.state.auth.user.token && this.$store.state.auth.user.userId) {
      this.$store.dispatch('auth/getSubBoard');
      this.actFavorites(this.$store.state.auth.user.userId).then(() => this.addSeggest());
    }
    this.actFollowRank();
    this.actPostRank();
    this.actLikeRank();
    this.actCommentRank();
  },
  computed: {
    ...mapGetters('main', ['getFavorites', 'getFollowRank', 'getPostsRank', 'getLikeRank', 'getCommentRank']),
    ...mapGetters('auth', ['getSubBoardFavoriteList', 'getSubBoardList']),
  },
  watch: {
    getFavorites: function() {
      this.favorites = this.getFavorites;
    },
    getLikeRank: function() {
      this.forceRerender();
    },
    getCommentRank: function() {
      this.forceRerender();
    },
  },
  methods: {
    ...mapActions('main', [
      'actFavorites',
      'actFollowRank',
      'actPostRank',
      'actLikeRank',
      'actCommentRank',
      'actNewBoards',
    ]),
    forceRerender() {
      this.componentKey += 1;
    },
    //메인 빈 공간 메우기위해 최신 보드 추가( 최소 8개 의 슬라이드 유지)
    addSeggest() {
      this.actNewBoards().then((result) => {
        const boards = result;
        for (let i = 0; i < boards.length; i++) {
          if (!this.$store.getters['auth/getSubscribed'](boards[i].board_id)) {
            //suggest에 사용할 보드 추가
            boards[i].type = 'suggest';
            this.favorites.push(boards[i]);
            if (this.favorites.length >= 8) break;
          }
        }
      });
    },
    clickFavorite: function(index) {
      this.$router.push(`board/${this.getFavorites[index].board_id}`);
    },
    clickCBtn1: function() {
      this.$router.push({ name: 'StudyMain' });
    },
    clickCBtn2: function() {
      this.$router.push({ name: 'LearnShare' });
    },
    clickCBtn3: function() {
      this.$router.push({ name: 'Recruitment' });
    },
    clickCBtn4: function() {
      this.$router.push({ name: 'Market' });
    },
    clickCBtn5: function() {
      this.$router.push({ name: 'BambooForest' });
    },
    clickPBtn1: function() {
      this.$store.commit('search/SET_PAYLOAD', {
        keyword: '',
        page: 0,
        type: 'name',
      });
      this.$store.commit('search/CLEAR_SEARCH_LIST');
      this.$store.commit('search/SET_SEARCH_ACTIVE', {
        active: 'allBoard',
        epicenter: '/',
      });
      this.$router.push({ name: 'SearchBoard' });
    },
    clickPBtn2: function() {
      this.$store.commit('search/SET_PAYLOAD', {
        keyword: '',
        page: 0,
        type: 'name',
      });
      this.$store.commit('search/CLEAR_SEARCH_LIST');
      this.$store.commit('search/SET_SEARCH_ACTIVE', {
        active: 'allPost',
        epicenter: '/',
      });
      this.$router.push({ name: 'SearchPost' });
    },
  },
};
</script>

<style scoped>
/*-------------------------------------------------- */
/* 공통 영역 */
/*-------------------------------------------------- */
p {
  margin: 0;
}
.main-title {
  margin: 0 80px;
  font-family: 'Nunito Sans', sans-serif;
  font-style: italic;
  font-size: 24px;
  font-weight: 200;
}
@media (max-width: 1535px) {
  .main-title {
    margin: 0 40px;
  }
}
@media (max-width: 768px) {
  .main-title {
    margin: 0 15px;
  }
}
@media (max-width: 640px) {
  .main-title {
    margin: 0 10px;
  }
}

@media (max-width: 426px) {
  .m-top {
    margin-top: 0px;
  }
}
/*-------------------------------------------------- */
/* 즐겨찾기 영역 */
/*-------------------------------------------------- */
#favorite-container {
  width: 100%;
}
#f-star {
  position: absolute;
  z-index: 1;
  margin: 12px;
  fill: var(--basic-color-new);
}
#f-swiper {
  margin-top: -10px;
  z-index: 0;
}
@media (max-width: 426px) {
  #f-swiper {
    margin-top: 0px;
  }
}
.swiper-slide {
  position: relative;
}
.swiper-slide:after {
  content: '';
  display: block;
  padding-bottom: 130%;
}

/*-------------------------------------------------- */
/* curation 영역 */
/*-------------------------------------------------- */
#curation-container {
  margin: 0 auto;
  max-width: 820px;
  padding: 0 30px;
  white-space: nowrap;
  overflow-x: auto;
  overflow-y: hidden;
}
/* @media (max-width: 1023px) {
  #curation-container {
    margin-top: 20px;
    padding: 0px 15px;
  }
} */
#c-title {
  position: absolute;
  font-size: 20px;
  padding-top: 48px;
  margin-top: 15px;
  width: 200px;
  text-align: center;
  z-index: 0;
}
@media (max-width: 426px) {
  #c-title {
    display: block;
    margin-left: calc(50% - 115px);
    font-size: 16px;
    padding-top: 0;
    z-index: 2;
  }
}
#c-list {
  position: relative;
  display: inline-block;
  padding-top: 50px;
  padding-bottom: 30px;
  margin-right: auto;
  margin-left: 0;
  z-index: 1;
  background: var(--basic-color-bg); /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #ebebe900 150px, #ebebe9ff 200px); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(
    to right,
    #ebebe900 150px,
    #ebebe9ff 200px
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}
#c-list button:first-child {
  margin-left: 200px;
}
@media (max-width: 426px) {
  #c-list {
    padding-top: 40px;
    padding-bottom: 30px;
  }
  #c-list button:first-child {
    margin-left: 0;
  }
}
.c-btn {
  width: 100px;
  height: 50px;
  font-weight: 700;
  margin-left: 15px;
  border: none;
  border-radius: 30px;
  transition: all 0.6s ease !important;
}
.c-btn:hover,
.c-btn:active {
  background: #ebebe9;
  background: linear-gradient(145deg, #d4d4d2, #fbfbf9);
  box-shadow: 10px 10px 20px #b3b3b1, -10px -10px 20px #ffffff;
}

/*-------------------------------------------------- */
/* 인기보드 영역 */
/*-------------------------------------------------- */
#popular-container {
  width: 90%;
  margin: 0 5%;
}
#p-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.container-m-r {
  margin-right: 1.65%;
}
.p-button {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 14px;
  padding: 4px 8px;
  border: 1px solid #000;
  border-radius: 30px;
  transition: background-color 0.3s, color 0.3s ease;
}
.p-button:hover,
.p-button:active {
  color: #fff;
  background-color: #000 !important;
}

.p-item-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.p-item {
  width: 32.5% !important;
  margin-bottom: 10px;
}
@media (max-width: 768px) {
  .p-item-container {
    flex-direction: column;
  }
  .p-item {
    width: 100% !important;
  }
  .container-m-r {
    margin-right: 0;
  }
  .p-button {
    margin-left: 5px;
    margin-top: 5px;
    font-size: 13px;
    padding: 3px 7px;
    border: 1px solid #000;
    border-radius: 30px;
    transition: background-color 0.3s, color 0.3s ease;
  }
}
#sub-title-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.p-desc {
  margin: 0;
  width: 50%;
}
.mobile {
  display: none;
}

/* pupular 내 인기 게시물 */
.line {
  width: 50%;
  margin: 15px auto 10px;
}
#p-item-container2 {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 50px;
}
.p-item2 {
  width: 49.2%;
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  #sub-title-container {
    display: none;
  }
  #p-item-container2 {
    flex-direction: column;
  }
  .p-item2 {
    width: 100% !important;
  }
  .mobile {
    display: inherit;
  }
}
</style>
