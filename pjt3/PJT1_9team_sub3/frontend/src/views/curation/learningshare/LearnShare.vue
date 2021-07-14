<template>
  <v-app>
    <v-main class="main-bg-color">
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
          <!-- 왼쪽 학습공유 설명 부분 -->
          <v-col class="col-12 col-sm-4">
            <div id="description" class="rounded-bg container-description">
              <!--curation 설명-->
              <h4 class="b-desc">학습공유</h4>
              <p class="l-desc">
                오늘의 학습 내용<br />
                함께 나누고픈 내용 공유
              </p>
              <!-- rank-->
              <v-divider class="my-2"></v-divider>
              <v-list-item
                ><a id="scrap-item" v-b-toggle href="#rank-collapse" @click.prevent
                  >RANK <b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
              ></v-list-item>
              <b-collapse visible id="rank-collapse">
                <v-col>
                  <LearningRank @prize-nickname="editMessage" />
                </v-col>
                <v-list-item>
                  RANK 1위의 한마디
                  <v-spacer></v-spacer>
                  <v-icon v-if="isPrize" @click="isEdit = !isEdit">mdi-circle-edit-outline</v-icon>
                </v-list-item>
                <v-col class="font-weight-black text-center">
                  <div v-if="!isEdit" v-html="first.speech"></div>
                  <!-- 1등한테 보여줄 input -->
                  <v-text-field v-if="isEdit" v-model="message" solo @keypress.enter="showMessage"></v-text-field>
                </v-col>
                <v-col class="text-end text-caption"> -{{ first.nickName }}- </v-col>
              </b-collapse>
              <!--워드클라우드-->
              <v-divider class="my-2"></v-divider>
              <v-list-item
                ><a id="scrap-item" v-b-toggle href="#wordcloud-collapse" @click.prevent
                  >WordCloud <b-icon icon="chevron-down" aria-hidden="true"></b-icon></a
              ></v-list-item>
              <b-collapse visible id="wordcloud-collapse">
                <v-col class="d-flex justify-center p-0">
                  <wordcloud
                    :data="defaultWords"
                    nameKey="name"
                    valueKey="value"
                    color="Category10"
                    :margin="wordcloudmargin"
                    :wordClick="wordClickHandler"
                  >
                  </wordcloud>
                </v-col>
              </b-collapse>
            </div>
          </v-col>
          <!-- 오른쪽 학습공유 본문 부분 -->
          <v-col class="col-12 col-sm-8">
            <!-- 학습공유 게시글쓰기 -->
            <!-- <LearningPostWrite class="mx-4 mb-2"/> -->
            <PostWrite :in-board="inBoard" style="margin:0 10px" />
            <!-- 학습공유 게시물 부분 -->
            <LearningSharePostList />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
// rank 리스트
import LearningRank from '@/components/curation/learningshare/LearningRank.vue';
// 학습공유 게시물
// import LearningPost from "@/components/curation/learningshare/LearningPost.vue"
// 학습공유 게시물 쓰기
// import LearningPostWrite from "@/components/curation/learningshare/LearningPostWrite.vue"
// 학습공유 리스트
import LearningSharePostList from '@/components/board/PostList';
import PostWrite from '@/components/board/PostWrite';

//워드클라우드
import wordcloud from 'vue-wordcloud';
import * as learnshareApi from '@/api/study';

export default {
  name: 'LearningShare',
  components: {
    LearningRank,
    // LearningPost,
    // LearningPostWrite,
    LearningSharePostList,
    PostWrite,
    wordcloud,
  },
  created() {
    learnshareApi
      .getWordCloud()
      .then((res) => {
        res.data.defaultWords.forEach((element) => {
          this.defaultWords.push({ name: element.name, value: element.score });
        });
      })
      .catch((err) => {
        console.log(err);
      });
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
  computed() {},
  data() {
    return {
      defaultWords: [],
      wordcloudmargin: { top: 15, right: 15, bottom: 15, left: 15 },
      // 모바일 화면 체크 mobile화면인지, 사이즈 이용할 값
      ResponsiveSize: {
        isMobile: false,
        viewSize: 0,
      },
      // 검색 키워드
      searchKeyword: '',
      // 한마디 v-model
      message: '',
      // rank1위 한마디
      first: {
        // rank 1위
        nickName: '',
        speech: `"아직 한마디가 없군요!"`,
      },
      // edit flag
      isEdit: false,
      isPrize: false,

      wordcloudImg: '@/assets/images/wordcloud.jpg',

      //변수 사용할 것들
      inBoard: true,
    };
  },
  methods: {
    // 현재 활성화된 기기에 따라 flag 변경
    onResize() {
      this.ResponsiveSize.isMobile = window.innerWidth < 426;
      this.ResponsiveSize.viewSize = window.innerWidth;
    },
    wordClickHandler(name, value, vm) {
      console.log('wordClickHandler', name, value, vm);
    },
    // 랭킹 1위 편집 가능케
    editMessage(nickName) {
      const first = nickName;
      if (first) {
        this.first.nickName = `${first}`;
        this.first.speech = `축하합니다 "${first}님"<br>한마디 부탁드려요!`;
        if (first === String(localStorage.nickname)) {
          this.isPrize = true;
        }
      }
    },
    // 내용 입력
    showMessage() {
      this.isEdit = !this.isEdit;
      this.first.speech = this.message.trim();
      this.message = this.first.speech;
    },
  },
};
</script>

<style scoped>
.main-bg-color {
  background-color: var(--basic-color-bg) !important;
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
