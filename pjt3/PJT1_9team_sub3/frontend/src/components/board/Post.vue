<template>
  <v-card id="container" class="pa-2 ani-hover">
    <div id="post-detail">
      <!-- 포스트 디테일 헤더 부분 -->
      <div id="header">
        <!-- 클릭시 드롭다운 -->
        <v-menu bottom left>
          <template v-slot:activator="{ on, attrs }">
            <v-btn depressed text v-bind="attrs" v-on="on" class="px-0">
              <div id="header-user-info">
                <!-- 프로필 사진 연결하기 -->
                <div>
                  <Profile id="profile-image" :getUserImage="image" />
                </div>
                <!-- 작성자이름, 작성일자 -->
                <div id="header-info">
                  <div>{{ post.user_nickname }}</div>
                  <div>{{ date }}</div>
                </div>
              </div>
            </v-btn>
          </template>

          <v-list>
            <!-- 프로필 보기 -->
            <v-list-item-group>
              <!-- 메세지 보내기(나와 채팅 금지) -->
              <v-list-item @click="onChat" v-if="!flagWriter">
                <v-list-item-title>
                  메세지 보내기
                </v-list-item-title>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>

        <!-- 수정 삭제 신고 버튼과 판매상태 정보 -->
        <div id="header-right" class="mr-3">
          <!-- 판매정보 부분 -->
          <div v-if="flagComponent.state">
            <v-chip v-if="this.post.post_state === 0" id="state-sale">
              판매중
            </v-chip>
            <v-chip v-else-if="this.post.post_state === 1" id="state-book">
              예약중
            </v-chip>
            <v-chip v-else id="state-complete">
              판매완료
            </v-chip>
          </div>
          <!-- 수정,삭제,신고 -->
          <!-- <div>
            <v-menu
              bottom
              left
            >
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  icon 
                  x-small 
                  fab
                  v-bind="attrs"
                  v-on="on"
                >
                  <v-icon dark>mdi-dots-vertical</v-icon>
                </v-btn>
              </template>
              
              <v-list >
                <v-list-item-group>
                  <v-list-item>
                    <v-list-item-title>
                      신고
                    </v-list-item-title>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-menu>
          </div> -->
        </div>
      </div>
      <!-- 디테일 페이지로 들어가는 부분 -->
      <div id="content-go-detail" @click="goToDetail">
        <!-- 포스트 제목 -->
        <div class="detail-header" v-if="flagComponent.headerLearnShare">
          <v-chip id="skill" v-for="(h, idx) in skill" :key="idx">
            {{ h }}
          </v-chip>
        </div>
        <div id="title">
          <!-- 중고장터용(지역) -->
          <div v-if="flagComponent.headerMarket">
            <v-chip outlined pill color="#695C4C" class="mr-3">
              {{ this.post.post_header }}
            </v-chip>
          </div>
          <div>{{ post.post_title }}</div>
          <div class="ml-1" id="is-image" v-if="post.post_image != ''">
            <i class="far fa-image"></i>
          </div>
        </div>
        <!-- 게시글 내용 -->
        <div id="description">
          <p id="sentence" v-for="(d, idx) in descriptions" :key="idx">
            {{ d }}
          </p>
          <!-- {{ post.post_description }} -->
        </div>
      </div>

      <!-- 게시글 관련 이미지/댓글/좋아요 들어갈 부분 -->
      <div id="actions">
        <!-- 댓글 수 -->
        <div id="bottom-comment-like">
          <!-- <v-icon
            middle
            class="mr-1"
            v-if="post.post_image"
          >mdi-image-filter</v-icon> -->

          <div id="bottom-comment">
            <v-icon middle class="mr-1">mdi-comment-outline</v-icon>
            <span v-if="isComment">{{ post.comment_count }}</span>
            <span v-else>0</span>
          </div>
          <!-- 좋아요 -->
          <div id="bottom-like">
            <button @click="postLike">
              <!-- 좋아요 중 -->
              <v-icon middle v-if="flagLike" color="#FFC400" class="mr-1">mdi-emoticon-excited</v-icon>
              <!-- 좋아요 취소상태 -->
              <v-icon middle class="mr-1" v-else>mdi-emoticon-neutral-outline</v-icon>
            </button>
            <span>{{ countLike }}</span>
          </div>
        </div>
        <!-- 북마크 -->
        <div>
          <button @click="postScrap">
            <!-- 북마크 중 -->
            <v-icon middle v-if="flagScrap" color="#0B2945">mdi-bookmark</v-icon>
            <!-- 북마크 취소상태 -->
            <v-icon middle v-else>mdi-bookmark-outline</v-icon>
          </button>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
// 프로필 이미지
import Profile from '@/components/etc/OtherProfile';

import * as postApi from '@/api/post';
import timeForToday from '@/plugins/timeForToday';

// 스타일 적용
import '@/assets/css/static/style.css';
// 채팅방 api
import * as chatApi from '@/api/chat';

export default {
  name: 'Post',
  components: {
    Profile,
  },
  props: {
    post: Object,
    flagComponent: Object,
  },
  data() {
    return {
      flagLike: false,
      flagScrap: false,

      test: String(localStorage.userId),
      countLike: 0,

      // 추후에 연결해줘야하는 부분 - 이미지(프로필 사진)
      profileImg: 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460',
      // 추후에 연결해줘야하는 부분 - 게시글 내 이미지 포함 여부
      haveImg: true,
    };
  },
  computed: {
    onceLiked() {
      return Object.keys(this.post).includes('isLiked');
    },
    onceScrapped() {
      return Object.keys(this.post).includes('isScrapped');
    },
    isComment() {
      return Object.keys(this.post).includes('comment_count');
    },
    isLike() {
      return Object.keys(this.post).includes('like_count');
    },
    flagWriter() {
      return this.post.user_id === String(localStorage.userId);
    },
    date() {
      return timeForToday(this.post.post_date);
    },
    skill() {
      if (
        this.post.post_header != '전체' &&
        this.post.post_header != 'null' &&
        this.post.post_header != '' &&
        this.post.post_header != null
      ) {
        return this.post.post_header.split('|');
      }
      return [];
    },
    image() {
      return this.post.user_image;
    },
    // 띄어쓰기 만들기
    descriptions() {
      return this.post.post_description.split('\n');
    },
  },
  mounted() {
    this.fetchData();
  },

  methods: {
    fetchData() {
      if (this.onceLiked) {
        if (this.post.isLiked === 0) {
          this.flagLike = false;
        } else {
          this.flagLike = true;
        }
      }

      if (this.onceScrapped) {
        if (this.post.isScrapped === 0) {
          this.flagScrap = false;
        } else {
          this.flagScrap = true;
        }
      }

      this.countLike = this.post.post_like;
    },
    // 재사용의 핵심
    goToDetail() {
      // params : {name:string, params:{board_id,post_id}}

      let data = {
        name: '',
        params: {
          board_id: '',
          post_id: this.post.post_id,
        },
      };
      const curationName = this.$route.name;
      // console.log(curationName);
      if (curationName === 'Board' || curationName === 'Study') {
        data.params.board_id = Number(this.$route.params.board_id);
      } else {
        data.params.board_id = this.$store.state.curationId[curationName];
      }
      // if(curationName!="Board" && curationName!="Study"){
      // }else{
      // }

      if (curationName === 'Market') {
        data.name = 'MarketPost';
      } else if (curationName === 'LearnShare') {
        data.name = 'LearnSharePost';
      } else if (curationName === 'Recruitment') {
        data.name = 'RecruitmentPost';
      } else if (curationName === 'StudyMain') {
        data.name = 'StudyMainPost';
      } else if (curationName === 'Study') {
        data.name = 'StudyGroupPost';
      } else {
        data.name = 'Post';
      }

      // params를 이용해서 데이터를 넘겨줄 수 있다.
      this.$router.push(data);
    },
    postLike(e) {
      postApi
        .likePost({ user_id: localStorage.getItem('userId'), post_id: this.post.post_id })
        .then((res) => {
          // console.log(res);
          if (res.data.message === 'No Subscription') {
            alert('구독 후에 이용 가능합니다');
          } else {
            if (this.flagLike) {
              this.countLike -= 1;
            } else {
              this.countLike += 1;
            }
            this.flagLike = !this.flagLike;
          }
        })
        .catch((err) => {
          console.err(err);
        });

      // 포스트좋아하는거 카운트 바꾸기 위한 지금 이 방식은 bug가 존재합니다. (유저와 연동이 안되어 있기 때문)
    },

    postScrap() {
      postApi
        .scrapPost({ user_id: localStorage.getItem('userId'), post_id: this.post.post_id })
        .then((res) => {
          if (res.data.message === 'No Subscription') {
            alert('구독 후에 이용 가능합니다');
          } else {
            this.flagScrap = !this.flagScrap;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 채팅으로 이동
    onChat() {
      // 중복 처리 해줘야 한다 - 있으면 기존껄로 없으면 만들기
      const params = {
        my_id: localStorage.getItem('userId'),
        opp_id: this.post.user_id,
      };
      chatApi
        .createChatRoom(params)
        .then((res) => {
          // console.log(res);
          if (res.data.message === 'fail') {
            // 대화한 적 있다라는 메세지 보여주기
            this.$toast.open({
              message: `기존의 대화방 이동`,
              type: 'success',
              duration: 1000,
              position: 'top-right',
            });
          } else {
            this.$toast.open({
              message: `새 대화방 생성`,
              duration: 1000,
              position: 'top-right',
            });
          }
          // 그리고 라우터 변환
          this.$router.push({ name: 'ChatPage' });
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
</script>

<style scoped>
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px !important;
  /* background-color: var(--basic-color-bg2) !important; */
  margin: 16px 10px;
}
/* 전체 detail 담겨진 부분 */
#post-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 0;
  padding: 1% 2%;
}
/* 프로필 이미지 */
#profile-image {
  width: 40px;
  height: 40px;
}
/* 전체 최상단(유저 & 판매상태 & 신고) */
#header {
  margin: 1%;
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
/* 사용자 정보 클릭시 드롭다운 연결 */
.v-menu__content {
  transform: translate(5px, 40px);
}
.v-list {
  padding: 0;
}

/* 프로필, 닉네임, 작성일  */
#header-user-info {
  display: flex;
  flex-direction: row;
  align-items: center;
}
/* 작성자 닉네임, 작성일 */
#header-info {
  height: 70%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 0.6em;
}
/* 닉네임 */
#header-info > div:first-child {
  line-height: 110%;
  font-size: 14px;
  font-weight: 400;
}
/* 작성일자 */
#header-info > div:last-of-type {
  line-height: 110%;
  font-size: 12px;
  font-weight: 300;
}
/* 헤더의 오른쪽 - 판매정보, 신고버튼 */
#header-right {
  display: flex;
  flex-direction: row;
}
/* 판매정보 */
#state-sale {
  background-color: #0b2945;
  color: #fff;
  border-radius: 10%;
}
#state-book {
  background-color: #aa2610;
  color: #fff;
  border-radius: 10%;
}
#state-complete {
  background-color: #f9f9f9;
  color: #000;
  border-radius: 10%;
}
/* 게시글 링크 정보 */
#content-go-detail {
  width: 100%;
  cursor: pointer;
  height: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 3; /* 라인수 */
  -webkit-box-orient: vertical;
  /* word-wrap:break-word;  */
  word-break: break-all;
  margin-bottom: 8px;
}
.detail-header {
  transform: translate(10px, 10px);
}
#skill {
  font-size: 14px;
  height: 20px;
  margin-right: 5px;
  color: #f9f9f9;
  background: #0b2945;
}
/* 게시글 제목 */
#title {
  margin: 2% 0 2% 1%;
  display: flex;
  flex-direction: row;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}
/* 사진유무 */
.is-image {
  transform: translate(0, 30px);
}
/* 게시글 내용 */
#description {
  margin: 0 0 1% 1%;
  font-size: 16px;
}
#sentence {
  margin: 0px;
}
/* 댓글, 좋아요, 북마크 부분 */
#actions {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
/* 댓글 좋아요 부분 */
#bottom-comment-like {
  display: flex;
  flex-direction: row;
}
#bottom-comment {
  display: flex;
  align-items: center;
  margin-right: 5px;
}
#bottom-like {
  display: flex;
  align-items: center;
}
.ani-hover {
  transition: transform 0.5s ease;
}
.ani-hover:hover {
  transform: scale(1.01);
}
</style>
