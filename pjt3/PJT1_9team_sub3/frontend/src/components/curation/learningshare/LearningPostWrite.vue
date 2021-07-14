<template>
  <v-card
    class="mx-auto
      my-0
      d-flex
      flex-column"
    color="F9F9F9"
    height="50px"
  >
      <v-dialog
        v-model="dialog"
        fullscreen
        hide-overlay
        transition="dialog-bottom-transition"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            block
            v-bind="attrs"
            v-on="on"
          >
            <div class="text-button">글쓰기</div>
          </v-btn>
        </template>
        <v-card>
          <!-- 상단 글쓰기 취소할 수 있는 부분 -->
          <v-toolbar
            dark
            color="#0B2945"
          >
            <v-btn
              icon
              dark
              @click="dialog = false"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
            <v-toolbar-title>학습공유</v-toolbar-title>
          </v-toolbar>
          <v-app class="main-bg-color">
            <!-- 전체 포함하는 컨테이너 -->
            <v-container
              class="pt-8"
            >
              <!-- 게시글 작성하기 제목 -->
              <div 
                class="text-overline 
                  text-center
                  font-weight-black"
                style="font-size: 24px !important;"
              >글쓰기</div>
              <v-row no-gutters dense>
                <v-col 
                  class="col-12">
                  <!-- 머리말 부분 -->
                  <v-col>
                    <div class="my-2">머리말</div>
                    <v-text-field
                      placeholder="ex. SSAFY"
                      solo
                      v-model.trim="hashtag"
                      @keypress.enter="addHashtag()"
                    ></v-text-field>
                    <!-- 머리말 보여지는 부분 -->
                    <div>
                      <v-chip-group
                        v-for="(hashtag, idx) in hashtags"
                        :key="idx">
                        <v-chip
                          class="ma-2"
                          close
                          color="indigo darken-4"
                          text-color="white"
                          @click:close="deleteHashtag(idx)"
                        > {{ hashtag }}
                        </v-chip>
                      </v-chip-group>
                    </div>
                  </v-col>
                  <!-- 게시글 이름 -->
                  <v-col>
                    <div class="my-2">제목</div>
                    <v-text-field
                      placeholder="게시글 제목"
                      solo
                      v-model="title"
                    ></v-text-field>
                  </v-col>
                  <!-- 게시글 내용 -->
                  <v-col>
                    <div class="my-2">내용</div>
                    <v-textarea
                      filled
                      solo
                      rows="10"
                      auto-grow
                      placeholder="게시글 내용"
                      v-model="description"
                      class="description"
                    ></v-textarea>
                  </v-col>
                  <v-col>
                    <div class="my-2">이미지</div>
                    <v-file-input class="d-flex"
                      filled
                      solo
                      prepend-icon="mdi-image"
                      placeholder="이미지 파일 선택"
                    ></v-file-input>
                  </v-col>
                </v-col>
              </v-row>
              <v-row 
                no-gutters 
                dense 
                class="d-flex 
                  justify-center">
                <v-btn
                  color="blue-grey"
                  x-large
                  class="ma-2 white--text text-overline"
                  @click="dialog = false"
                >
                  작성 완료
                </v-btn>
              </v-row>
            </v-container>
          </v-app>
        </v-card>
      </v-dialog>
  </v-card>
</template>

<script>
import * as boardApi from "@/api/board";

export default {
  name: "LearningPostWrite",
  data() {
    return {
      // test용(모달)
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,


      switch1: true,
      title:'',
      description:'',
      file1:[],
      hashtag:'',
      hashtags:[],
      location:'',
      selected: [],
      options: {
        f:[
          {text:'인기글',value:'popular',disabled:false},
          {text:'랭킹',value:'rank',disabled:false},
          {text:'체크리스트',value:'checklist',disabled:true},
          {text:'달력',value:'calender',disabled:true},
          {text:'투표',value:'vate',disabled:true},
        ],
        location: ['서울', '대전', '구미', '광주'],
        ikmyeong:false,
      },
    }
  },
  methods: {
    addHashtag() {
      if(this.hashtags.length===5){
        alert('해쉬태그는 5개 이하만 가능합니다.')
        return
      }
      this.hashtags.push(`#${this.hashtag}`)
      this.hashtag=''
    },
    deleteHashtag(e){
      this.hashtags.splice(e,1)
    },
    onCreate(){
      //@param : user_id, board_name, board_description, board_location, board_igmyeong, board_hash, checklist_flag, calendar_flag, vote_flag
      let board={
        user_id: localStorage.getItem('userId'),
        board_name: this.title,
        board_description: this.description,
        board_location: '전체',
        board_igmyeong: 0,
        board_hash:this.hashtags.join('|'),
        checklist_flag:0,
        calendar_flag:0,
        vote_flag:0
      };
      boardApi.board_create(board).then(response => {
        console.log(response.data);
        const subBoard = JSON.parse(localStorage.subBoard)
        subBoard.push({
          board_id:response.data.board_id,
          user_id:localStorage.userId,
          favorite_flag:0,
          is_used:1,
          user_role:1,
          write_post_count:0
        })
        localStorage.subBoard = JSON.stringify(subBoard)
        alert('게시판 보드 생성')
        
        this.$router.push({name:'Board',params:{board_id:response.data.board_id}})
      }).catch(error => {
        console.log(error);
        alert("보드 생성에 실패하였습니다.");
      })
    }
  }
}
</script>

<style scoped>

</style>