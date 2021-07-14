<template>
  <v-app id="inspire">
    <v-main class="main-bg-color">
      <v-container class="pt-8">
        <v-row>
          <!-- 모바일 화면 -->
          <!-- 좌측 내 정보 부분 -->
          <v-col cols="12" v-if="ResponsiveSize.isMobile">
            <v-card color="#fcfcfc" id="custom-container">
              <v-list color="transparent">
                <!-- 내 정보 부분 -->
                <v-list-item
                  color="grey 
                  lighten-4"
                  class="d-flex
                  flex-row
                  justify-space-between
                  pa-4"
                >
                  <!-- 프로필 사진 연결하기 -->
                  <v-avatar :size="ResponsiveSize.viewSize * 0.3" class="flex-shrink-1">
                    <Profile id="profile-image" />
                    <div class="profile-imgae-edit" v-if="myInfo.myInfoEdit" @click="clickProfile" />
                  </v-avatar>
                  <!-- 회원이 입력한 정보 -->
                  <div
                    class="ma-4
                    d-flex
                    flex-column
                    align-start"
                  >
                    <!-- 닉네임 -->
                    <div class="text-h5" v-if="!myInfo.myInfoEdit">
                      {{ myInfo.nickname }}
                    </div>
                    <div v-if="myInfo.myInfoEdit">
                      <v-text-field
                        dense
                        label="nickname"
                        v-model="myInfo.nickname"
                        class="text-h5"
                        color="grey-darken-4"
                      ></v-text-field>
                    </div>
                    <!-- 이메일 -->
                    <div id="email-div">
                      {{ myInfo.email }}
                    </div>

                    <!-- 지역 & 기수 -->
                    <div v-if="!myInfo.myInfoEdit">{{ myInfo.location }} {{ myInfo.generation }}기</div>
                    <div v-if="myInfo.myInfoEdit">
                      <v-row>
                        <v-col>
                          <v-select v-model="myInfo.location" :items="myInfo.options.location" label="지역"></v-select>
                        </v-col>
                        <v-col>
                          <v-select
                            v-model="myInfo.generation"
                            :items="myInfo.options.generation"
                            label="기수"
                          ></v-select>
                        </v-col>
                      </v-row>
                    </div>
                  </div>
                  <!-- edit 버튼('나'일 때만 보여주기) -->
                  <div id="edit-btn" class="align-self-start">
                    <v-btn
                      icon
                      outlined
                      x-small
                      fab
                      :color="myInfo.myInfoEdit ? '#AA2610' : '#0B2945'"
                      @click="editMyInfo()"
                    >
                      <v-icon dark>
                        mdi-pencil
                      </v-icon>
                    </v-btn>
                  </div>
                </v-list-item>
                <!-- 비밀번호 변경 부분 -->
                <PasswordChange />
                <AccountDelete v-if="myInfo.myInfoEdit" />
                <v-divider class="my-2"></v-divider>
                <!-- 이동 탭 부분 -->
                <v-list-item-group
                  class="d-flex
                  flex-row mx-2"
                  v-model="ResponsiveSize.whichTapPoint"
                  mandatory
                >
                  <v-list-item
                    v-for="[icon, text, index] in links"
                    :key="text"
                    link
                    class="px-0 py-1
                    ma-1"
                    @click="clickMobileTap(index)"
                  >
                    <!-- 모바일 탭 전환 -->
                    <v-list-item-content
                      class="d-flex
                      flex-row
                      justify-center
                      ma-1
                      px-1
                      py-0"
                    >
                      <v-icon
                        class="pa-2
                        ma-1"
                      >
                        {{ icon }}
                      </v-icon>
                      <div style="font-size: 12px;" class="text-center">
                        {{ text }}
                      </div>
                    </v-list-item-content>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-card>
          </v-col>

          <!-- PC화면 -->
          <!-- 좌측 내 정보 부분 -->
          <v-col sm="3" v-else>
            <v-sheet color="#fcfcfc" id="custom-container">
              <v-list color="transparent">
                <div
                  color="grey 
                  lighten-4"
                  class="d-flex
                  flex-column
                  justify-space-between
                  pa-4"
                >
                  <!-- 내 정보 부분 -->
                  <div
                    class="d-flex
                    flex-row
                    justify-space-between"
                  >
                    <!-- 내 정보 타이틀 -->
                    <div class="text-h5">내 정보</div>
                    <!-- 내 정보 수정버튼 -->
                    <div>
                      <v-btn
                        icon
                        outlined
                        x-small
                        fab
                        :color="myInfo.myInfoEdit ? '#AA2610' : '#0B2945'"
                        @click="editMyInfo()"
                      >
                        <v-icon>
                          mdi-pencil
                        </v-icon>
                      </v-btn>
                    </div>
                  </div>
                  <!-- 프로필 이미지 -->
                  <v-avatar :size="ResponsiveSize.viewSize * 0.1" class="align-self-center">
                    <Profile id="profile-image" />
                    <ImageUpload class="profile-imgae-edit" v-if="myInfo.myInfoEdit" @click="clickProfile" />
                    <!-- <div class="profile-imgae-edit" v-if="myInfo.myInfoEdit" @click="clickProfile" /> -->
                  </v-avatar>

                  <!-- 닉네임 -->
                  <div
                    v-if="!myInfo.myInfoEdit"
                    class="mt-4
                    text-h5 
                    font-weight-black"
                  >
                    {{ myInfo.nickname }}
                  </div>
                  <div v-if="myInfo.myInfoEdit">
                    <v-text-field
                      dense
                      label="nickname"
                      v-model="myInfo.nickname"
                      class="text-h5"
                      color="grey-darken-4"
                    ></v-text-field>
                  </div>
                  <!-- 이메일 부분 -->
                  <div>
                    {{ myInfo.email }}
                  </div>

                  <!-- 지역 & 기수 -->
                  <div v-if="!myInfo.myInfoEdit">{{ myInfo.location }} {{ myInfo.generation }}기</div>
                  <div v-if="myInfo.myInfoEdit">
                    <v-row>
                      <v-col>
                        <v-select v-model="myInfo.location" :items="myInfo.options.location" label="지역"></v-select>
                      </v-col>
                      <v-col>
                        <v-select
                          v-model="myInfo.generation"
                          :items="myInfo.options.generation"
                          label="기수"
                        ></v-select>
                      </v-col>
                    </v-row>
                  </div>
                </div>
                <!-- 비밀번호 변경 부분 -->
                <PasswordChange />
                <AccountDelete v-if="myInfo.myInfoEdit" />

                <v-divider class="my-2"></v-divider>

                <!-- 이동 탭 부분 -->
                <v-list-item-group v-model="ResponsiveSize.whichTapPoint" mandatory class="px-2 py-0">
                  <v-list-item
                    v-for="[icon, text, index] in links"
                    :key="text"
                    link
                    @click="clickMobileTap(index)"
                    style="border-radius: 15px"
                  >
                    <!-- 모바일 탭 전환 -->
                    <v-list-item-icon>
                      <v-icon>{{ icon }}</v-icon>
                    </v-list-item-icon>
                    <v-list-item-title>{{ text }}</v-list-item-title>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-sheet>
          </v-col>

          <v-col sm="9">
            <v-sheet min-height="85vh" color="#fcfcfc" id="custom-container" class="pa-2">
              <!-- 내 구독보드 -->
              <div v-if="mobileTap[0]">
                <v-card-title
                  class="text-h6
                  font-weight-black
                  pb-0"
                  >구독중인 보드</v-card-title
                >
                <v-divider></v-divider>
                <Subscription
                  v-for="board in subBoardList"
                  :key="board.board_id"
                  :board="board"
                  class="pa-2 ani-hover"
                  @delSub="deleteSub"
                />
                <!-- <Subscription />
                <Subscription /> -->
              </div>

              <!-- 내 작성글 -->
              <div v-if="mobileTap[1]">
                <v-card-title
                  class="text-h6
                  font-weight-black
                  pb-0"
                  >내 작성글</v-card-title
                >
                <v-divider></v-divider>
                <MyPost
                  v-for="post in getsPosts"
                  :key="post.post_id"
                  :post="post"
                  class="pa-2 ani-hover"
                  @delPost="deletePost"
                />
              </div>
              <!-- 내 댓글 -->
              <div v-if="mobileTap[2]">
                <v-card-title
                  class="text-h6
                  font-weight-black
                  pb-0"
                  >내 작성댓글</v-card-title
                >
                <v-divider></v-divider>
                <MyComment
                  v-for="comment in getGetComments"
                  :key="comment.comment_id"
                  :comment="comment"
                  @delComment="delComment"
                  class="pa-2 ani-hover"
                />
              </div>

              <!-- 내가 스크랩한 글 -->
              <div v-if="mobileTap[3]">
                <v-card-title
                  class="text-h6
                  font-weight-black
                  pb-0"
                  >스크랩한 글</v-card-title
                >
                <v-divider></v-divider>
                <ScrapPost
                  v-for="scrap in getScraps"
                  :key="scrap.post_id"
                  :scrap="scrap"
                  class="pa-2 ani-hover"
                  @delScrap="delScrap"
                />
              </div>
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Profile from '@/components/etc/Profile';
import PasswordChange from '@/components/mypage/PasswordChange';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'mypage',
  components: {
    Profile,
    PasswordChange,
    AccountDelete: () => import('@/components/mypage/AccountDelete'),

    Subscription: () => import('@/components/mypage/Subscription'),
    MyPost: () => import('@/components/mypage/MyPost'),
    MyComment: () => import('@/components/mypage/MyComment'),
    ScrapPost: () => import('@/components/mypage/ScrapPost'),
    ImageUpload: () => import('@/components/mypage/ImageUpload'),
  },
  data() {
    return {
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false,
        viewSize: 0,
        whichTapPoint: 0,
      },
      // 모바일 화면 전환 플래그
      mobileTap: [true, false, false, false],
      // 모바일 화면 전환 탭
      links: [
        ['mdi-view-list', '내 구독보드', 0],
        ['mdi-newspaper-variant-multiple-outline', '내 작성글', 1],
        ['mdi-comment-text-multiple', '내 작성댓글', 2],
        ['mdi-bookmark-multiple', '내 스크랩', 3],
      ],
      // 내 정보 부분
      myInfo: {
        myInfoEdit: false,
        options: {
          location: ['서울', '대전', '구미', '광주'],
          generation: ['5', '4', '3', '2', '1'],
        },
      },
      //구독 보드 리스트
      subBoardList: [],
    };
  },
  // 뷰 인스턴스 제거될 때 resize 호출
  beforeDestroy() {
    if (typeof window === 'undefined') return;

    window.removeEventListener('resize', this.onResize, { passive: true });
  },
  // resize 실시해서 현재 화면 크기 확인
  mounted() {
    this.onResize();
    this.fetchData();
    window.addEventListener('resize', this.onResize, { passive: true });

    this.getSubBoard();
    this.subBoardList = this.getSubBoardList;
    this.getPosts();
    this.actGetComments();
    this.actScraps();
  },
  created() {
    this.getMyInfo();
  },
  computed: {
    ...mapGetters('auth', ['getsMyInfo', 'getSubBoardList']),
    ...mapGetters('user', ['getsPosts', 'getGetComments', 'getScraps']),
  },
  methods: {
    ...mapActions('auth', ['getMyInfo', 'putMyinfo', 'getSubBoard', 'actDelComment']),
    ...mapActions('user', [
      'putDeleteSub',
      'getPosts',
      'actDeletePost',
      'actGetComments',
      'actDelComment',
      'actScraps',
      'actTogleScrap',
    ]),
    //프로필 이미지 변경
    clickProfile() {
      alert('dd');
    },
    // 내 정보보기
    fetchData() {
      //myInfo에 데이터 패치
      this.myInfo.nickname = this.getsMyInfo.nickname;
      this.myInfo.email = this.getsMyInfo.email;
      this.myInfo.generation = this.getsMyInfo.generation;
      this.myInfo.location = this.getsMyInfo.location;
      this.myInfo.image = this.getsMyInfo.image;
    },

    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },

    clickMobileTap: function(point) {
      // 전체 false로 초기화
      this.mobileTap = this.mobileTap.map(function(el) {
        return false;
      });
      // 클릭하게 되면 pointing된 부분을 true로 변경
      this.mobileTap[point] = true;
      // 현재 가르키고 있는 탭 부분 변경
      this.ResponsiveSize.whichTapPoint = point;
    },
    editMyInfo() {
      if (this.myInfo.myInfoEdit == true) {
        const member = {
          user_email: this.myInfo.email,
          user_generation: this.myInfo.generation,
          user_image: this.$store.state.auth.user.image,
          user_location: this.myInfo.location,
          user_nickname: this.myInfo.nickname,
        };
        this.$store.dispatch('auth/putMyinfo', member).then((result) => {
          if (result) {
            alert('내 정보가 정상적으로 변경되었습니다.');
          } else {
            alert('내 정보 변경 중 문제가 발생했습니다.');
          }
          this.fetchData();
        });
      }
      this.myInfo.myInfoEdit = !this.myInfo.myInfoEdit;
    },
    deleteSub(board_id) {
      this.putDeleteSub({
        user_id: this.$store.state.auth.user.userId,
        board_id: board_id,
      }).then((result) => {
        //서버에서 잘 삭제될 경우 data 관리
        if (result) {
          const deleteIndex = this.subBoardList.findIndex((board) => {
            return board.board_id == board_id;
          });
          // this.subBoardList.splice(deleteIndex, 1);
          this.$delete(this.subBoardList, deleteIndex);
          this.$delete(this.$store.state.auth.subBoard, deleteIndex);

          //vuex에 있는 데이터 localStorge에 동기화
          this.$store.commit('auth/setSubBoardRefresh2');
        }
      });
    },
    deletePost(post_id) {
      this.actDeletePost({
        user_id: this.$store.state.auth.user.userId,
        post_id: post_id,
      }).then((result) => {
        if (result) {
          const deleteIndex = this.getsPosts.findIndex((post) => {
            return post.post_id == post_id;
          });
          this.$delete(this.getsPosts, deleteIndex);
        }
      });
    },
    delComment(comment_id) {
      this.actDelComment(comment_id).then((result) => {
        if (result) {
          const deleteIndex = this.getGetComments.findIndex((comment) => {
            return comment.comment_id == comment_id;
          });
          this.$delete(this.getGetComments, deleteIndex);
        }
      });
    },
    delScrap(post_id) {
      console.log(post_id);
      this.actTogleScrap(post_id).then((result) => {
        if (result) {
          const deleteIndex = this.getScraps.findIndex((scrap) => {
            return scrap.post_id == post_id;
          });
          console.log(this.getScraps[deleteIndex]);
          this.$delete(this.getScraps, deleteIndex);
        }
      });
    },
  },
};
</script>
<style scoped>
/* 전체 메인 배경색 */
.main-bg-color {
  background-color: #ebebe9;
}
#profile-image {
  width: 100%;
  height: 100%;
}
.profile-imgae-edit {
  position: absolute;
  z-index: 10;
  left: 0;
  cursor: pointer;
  width: 100%;
  height: 100%;
  background-color: #000;
  background-image: url(../../assets/images/image_upload_w.png);
  background-size: 30px 30px;
  background-position: center;
  border-radius: 50%;
  opacity: 0.4;
  transition: transform 0.5s, opacity 0.5s ease;
}
.profile-imgae-edit:hover {
  transform: scale(1.1);
  opacity: 0.8;
}
.theme--light.v-list-item:hover::before,
.theme--light.v-list-item::before {
  border-radius: 10px;
}
.theme--light.v-list-item--active:hover::before,
.theme--light.v-list-item--active::before {
  border-radius: 10px;
  background-color: var(--basic-color-key);
  box-shadow: 0 0 4px #000000;
}
#custom-container {
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px !important;
  background-color: var(--basic-color-bg2) !important;
}
.ani-hover {
  transition: transform 0.5s ease;
}
.ani-hover:hover {
  transform: scale(1.01);
}

/* 모바일 작은 화면 대응을 위해 css 추가 */
#edit-btn {
  position: absolute;
  margin-top: -10px;
  right: 14px;
}
#email-div {
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
</style>
