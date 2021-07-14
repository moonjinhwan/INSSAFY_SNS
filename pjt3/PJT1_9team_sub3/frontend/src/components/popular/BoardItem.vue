<template>
  <div id="wrap">
    <div id="popular-item">
      <div id="header">
        <div id="h-bg" :class="{ none: !imageState }" :style="{ backgroundImage: `url(${imageState})` }" />
        <GradientGenerator id="h-bg" :class="{ none: imageState }" />
        <div id="h-inner" @click="clickHeader">
          <!-- <p id="type" class="l-desc-e">{{ `Type` }}</p> -->
          <p id="board-name" class="b-desc">{{ item.board_name }}</p>
          <p id="follower" class="l-desc-e">
            <!-- <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
              <path
                d="M17.997 18h-11.995l-.002-.623c0-1.259.1-1.986 1.588-2.33 1.684-.389 3.344-.736 2.545-2.209-2.366-4.363-.674-6.838 1.866-6.838 2.491 0 4.226 2.383 1.866 6.839-.775 1.464.826 1.812 2.545 2.209 1.49.344 1.589 1.072 1.589 2.333l-.002.619zm4.811-2.214c-1.29-.298-2.49-.559-1.909-1.657 1.769-3.342.469-5.129-1.4-5.129-1.265 0-2.248.817-2.248 2.324 0 3.903 2.268 1.77 2.246 6.676h4.501l.002-.463c0-.946-.074-1.493-1.192-1.751zm-22.806 2.214h4.501c-.021-4.906 2.246-2.772 2.246-6.676 0-1.507-.983-2.324-2.248-2.324-1.869 0-3.169 1.787-1.399 5.129.581 1.099-.619 1.359-1.909 1.657-1.119.258-1.193.805-1.193 1.751l.002.463z"
              />
            </svg> -->
            <!-- {{ `get radis` }} -->
          </p>
        </div>
      </div>
      <div id="posts">
        <div id="p-inner">
          <div id="p-top"></div>
          <swiper v-once class="swiper" :options="swiperOption2" @clickSlide="clickTitle">
            <swiper-slide class="p-slide r-desc" v-for="el in item.board_posts" :key="el.post_id">
              <span>{{ el.post_title }}</span>
            </swiper-slide>
          </swiper>
          <div id="p-bottom"></div>
        </div>
        <div id="follow-togle">
          <b-icon id="f-icon" icon="heart-fill" aria-hidden="true" />
          <b-icon
            id="f-icon-active"
            :class="{ clear: !btnState }"
            icon="heart-fill"
            @click="clickFollow"
            aria-hidden="true"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
import 'swiper/css/swiper.css';
import { mapActions, mapGetters, mapState } from 'vuex';
import GradientGenerator from '@/components/etc/GradientGenerator';

export default {
  name: 'BoardItem',
  props: {
    type: String,
    item: Object,
  },
  components: {
    Swiper,
    SwiperSlide,
    GradientGenerator,
  },
  data() {
    return {
      imageState: false,
      subState: false,
      swiperOption2: {
        effect: 'coverflow',
        coverflowEffect: {
          rotate: 5,
          stretch: -5,
          depth: 100,
          modifier: 1,
          slideShadows: false,
        },
        grabCursor: true,
        freeMode: false,
        // threshold: 20,
        speed: 1000,
        autoplay: {
          delay: 1000,
        },
        on: {
          init: this.init,
          sliderMove: this.stopAutoplay,
          slideChangeTransitionState: this.stopAutoplay,
          transitionEnd: this.startAutoplay,
        },
        direction: 'vertical',
        slidesPerView: 5,
        spaceBetween: 0,
        loop: true,
      },
    };
  },
  created() {
    //이미지 상태 초기화
    if (this.item.board_image == undefined || this.item.board_image == 'null' || this.item.board_image == '') {
      this.imageState = false;
    } else {
      this.imageState = this.item.board_image;
    }
    // console.log(this.$store.getters['auth/getSubBoardList']);
    // console.log(this.getSubscribed(this.item.board_id));
  },
  computed: {
    ...mapState('auth', ['subBoard']),
    ...mapGetters('auth', ['getSubscribed']),
    //하트 버튼상태 vuex에서 가져옴
    btnState: function() {
      if (this.type === 'subscribe') {
        return this.$store.getters['main/getSubHeartBtns'](this.item.board_id);
      } else {
        return this.$store.getters['main/getPostHeartBtns'](this.item.board_id);
      }
    },
  },

  methods: {
    ...mapActions('user', ['putDeleteSub']),
    ...mapActions('board', ['actPostSubscribe']),
    clickHeader: function() {
      this.$router.push(`/board/${this.item.board_id}`);
    },
    clickTitle: function(index) {
      index = index % 5;
      this.$router.push(`/board/${this.item.board_id}/post/${this.item.board_posts[index].post_id}`);
    },
    //하트버튼 vues state 토글 메소드
    heartTogle: function() {
      //하트 클릭 vuex로 관리
      if (this.type === 'subscribe') {
        this.$store.commit('main/CLICK_SUB_HEART_BTNS', {
          board_id: this.item.board_id,
          state: this.btnState,
        });
      } else {
        this.$store.commit('main/CLICK_POST_HEART_BTNS', {
          board_id: this.item.board_id,
          state: this.btnState,
        });
      }
    },
    clickFollow: function() {
      if (this.btnState) {
        this.putDeleteSub({
          user_id: this.$store.state.auth.user.userId,
          board_id: Number(this.item.board_id),
        }).then((result) => {
          //서버에서 잘 삭제될 경우 data 관리
          if (result) {
            const deleteIndex = this.subBoard.findIndex((board) => {
              return board.board_id == this.item.board_id;
            });
            // this.subBoardList.splice(deleteIndex, 1);
            this.$delete(this.subBoard, deleteIndex);
            //localstorage에 동기화
            this.$store.commit('auth/setSubBoardRefresh2');

            //토스트 알림
            this.$toast.open({
              message: `${this.item.board_name} 보드 구독 취소`,
              type: 'warning',
            });
            this.heartTogle();
          } else {
            alert('구독 취소 중에 문제가 발생했습니다.');
          }
        });
      } else {
        this.actPostSubscribe(Number(this.item.board_id)).then((result) => {
          if (result) {
            this.$toast.open({
              message: `${this.item.board_name} 보드 구독 중`,
              type: 'success',
            });
            this.heartTogle();
          }
        });
      }
    },
  },
};
</script>

<style scoped>
#wrap {
  display: inline-block;
}
#popular-item {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100%;
}
#header {
  position: relative;
  width: 28%;
}
#header:after {
  content: '';
  display: block;
  padding-bottom: 100%;
}
#h-bg {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 1;
  background-image: url(../../assets/images/slide.jpg);
  background-position: center;
  background-size: cover;
}
#h-inner {
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #fff;
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 2;
  background-color: #00000055;
}
#h-inner svg {
  margin-top: -5px;
  fill: #fff;
}
#type {
  margin: 0;
  text-align: center;
}
#board-name {
  margin: 20% 0;
  position: relative;
  font-size: 24px;
  text-align: center;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
#follower {
  margin: 0;
  text-align: center;
}
@media (max-width: 426px) {
  #type {
    font-size: 12px;
  }
  #board-name {
    margin: 8% 0;
    font-size: 18px;
  }
  #follower {
    font-size: 12px;
  }
}

#posts {
  position: relative;
  width: 72%;
}
#posts:after {
  content: '';
  display: block;
  padding-bottom: 40%;
}
#p-inner {
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: absolute;
  background-color: var(--basic-color-bg2);
  width: 100%;
  height: 100%;
}
#p-top {
  width: 100%;
  height: 20%;
  position: absolute;
  top: 0;
  z-index: 2;
  background: var(--basic-color-bg2); /* fallback for old browsers */
  background: -webkit-linear-gradient(to bottom, #f9f9f9 20%, #f9f9f900); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(
    to bottom,
    #f9f9f9 20%,
    #f9f9f900
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}
#p-bottom {
  width: 100%;
  height: 20%;
  position: absolute;
  top: calc(100% - 20%);
  z-index: 1;
  background: var(--basic-color-bg2); /* fallback for old browsers */
  background: -webkit-linear-gradient(to top, #f9f9f9 20%, #f9f9f900); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(
    to top,
    #f9f9f9 20%,
    #f9f9f900
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}
#follow-togle {
  position: absolute;
  cursor: pointer;
  z-index: 5;
  top: 10px;
  right: 10px;
  transition: transform 0.4s ease;
}
#follow-togle:hover {
  transform: scale(1.2);
}

#f-icon {
  width: 28px;
  height: 28px;
  color: #aa2610;
  fill: #695c4c44;
}
#f-icon-active {
  position: absolute;
  left: 0;
  width: 28px;
  height: 28px;
  color: #aa2610;
  transition: opacity 0.3s ease-in-out;
}
@media (min-width: 769px) and (max-width: 1024px) {
  #f-icon {
    width: 20px;
    height: 20px;
  }
  #f-icon-active {
    left: 0;
    width: 20px;
    height: 20px;
  }
}

.p-slide {
  font-size: 30px;
  text-align: center;
}
.p-slide span {
  text-align: center;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.p-slide:hover {
  font-weight: 600;
  transform: scale(1.04) !important;
}
@media (max-width: 2000px) {
  .p-slide {
    font-size: 16px;
  }
  #type {
    font-size: 12px;
  }
  #board-name {
    margin: 8% 0;
    font-size: 18px;
  }
  #follower {
    font-size: 12px;
  }
}
@media (max-width: 1440px) {
  .p-slide {
    font-size: 16px;
  }
}
@media (max-width: 1100px) {
  .p-slide {
    font-size: 12px;
  }
}
@media (max-width: 768px) {
  .p-slide {
    font-size: 20px;
  }
}
@media (max-width: 768px) {
  .p-slide {
    font-size: 20px;
  }
}
@media (max-width: 426px) {
  .p-slide {
    font-size: 15px;
  }
}
</style>
