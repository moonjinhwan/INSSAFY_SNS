<template>
  <v-app class="main-bg-color">
    <!-- 전체 포함하는 컨테이너 -->
    <v-container
      class="pt-8"
    >
      <!-- 보드 생성하기 제목 -->
      <div 
        class="text-overline 
          text-center
          font-weight-black"
        style="font-size: 24px !important;"
      >스터디 생성하기</div>
      <v-row no-gutters dense>
        <!-- (PC기준)왼쪽 -->
        <v-col 
          class="col-12 col-sm-8">
          <!-- 보드 이름 -->
          <v-col>
            <div class="my-2">스터디 이름</div>
            <v-text-field
              placeholder="스터디이름을 작성해주세요"
              solo
              v-model="title"
            ></v-text-field>
          </v-col>
          <!-- 보드 설명 -->
          <v-col>
            <div class="my-2">스터디 설명</div>
            <v-textarea
              filled
              solo
              rows="4"
              row-height="30"
              placeholder="스터디에 대한 설명을 적어보세요"
              v-model="description"
              class="description"
            ></v-textarea>
          </v-col>
          <!-- 해쉬태그 -->
          <v-col>
            <div class="my-2">Hash Tag</div>
            <v-text-field
              placeholder="ex. SSAFY"
              solo
              v-model.trim="hashtag"
              @keypress.enter="addHashtag()"
            ></v-text-field>
            <!-- 해쉬태그 보여지는 부분 -->
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
        </v-col>
        <!-- (PC기준)오른쪽 -->
        <v-col class="col-12 col-sm-4">
          <!-- 보드 대표 이미지 -->
          <v-col>
            <div class="my-2">대표 이미지</div>
            <v-file-input class="d-flex"
              filled
              solo
              prepend-icon="mdi-image"
              placeholder="대표 이미지 선택하기"
            ></v-file-input>
          </v-col>
          <!-- 지역 정보 -->
          <v-col>
            <v-select
              v-model="location"
              :items="options.location"
              label="지역"
              solo
            ></v-select>
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
          @click="onCreate()"

        >
          스터디 만들기
        </v-btn>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import * as boardApi from '@/api/board';

export default {
  name:"BoardForm",
  data() {
    return {
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
        board_hash:this.hashtags.join('|'),
        board_igmyeong: 0,
        checklist_flag:0,
        calendar_flag:0,
        vote_flag:0,
        user_rank_flag:0,
        board_state:3
      };
      boardApi.board_create(board).then(response => {
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
        this.$router.push({name:'Study',params:{board_id:response.data.board_id}})
      }).catch(error => {
        alert("보드 생성에 실패하였습니다.");
      })
    }
  }
}
</script>

<style scoped>
/* 전체 메인 배경색 */
.main-bg-color {
  background-color: #ebebe9;
}
</style>