<template>
  <div class="main-bg-color">
    <!-- 전체 포함하는 컨테이너 -->
    <div class="join-form">
      <!-- <div 
        class="text-overline text-center font-weight-black mb-3"
        style="font-size: 24px !important;"
      >보드 생성하기
      </div> -->
      <h2 class="mb-3">스터디 생성</h2>
      <div class="join-input">
        <!-- 보드 생성하기 제목 -->
        <div>
          <!-- 보드 이름 -->
          <v-text-field label="스터디 이름" v-model="title" prepend-icon="T"></v-text-field>
          <!-- 보드 설명 -->
          <v-textarea label="상세설명" rows="4"
            row-height="30" prepend-icon="mdi-format-align-left" v-model="description"></v-textarea>
          <!-- 해쉬태그 -->
          <v-text-field
            label=" 해쉬태그"
            placeholder="ex. SSAFY"
            v-model.trim="hashtag"
            prepend-icon="#"
            @keypress.enter="addHashtag()"
          ></v-text-field>
          <!-- 해쉬태그 보여지는 부분 -->
          <div class="hash-group">
            <v-chip-group 
              v-for="(hashtag, idx) in hashtags"
              :key="idx">
              <v-chip
                class="ma-2"
                close
                color="#0b2945"
                text-color="#fff"
                @click:close="deleteHashtag(idx)"
              > {{ hashtag }}
              </v-chip>
            </v-chip-group>
          </div>
          <!-- 보드 대표 이미지 -->
          <v-file-input class="d-flex"
            v-model="file"
            prepend-icon="mdi-image"
            placeholder=" 대표 이미지 선택 (1X4 비율 추천)"
          ></v-file-input>
        </div>
          
      </div>
      <v-row 
        no-gutters 
        dense 
        class="d-flex 
          justify-center">
        <v-btn
          x-large
          class="btn-write newmorphism"
          @click="onCreate()"
        >
          보드 만들기
        </v-btn>
      </v-row>
    </div>
  </div>
</template>

<script>
import * as boardApi from '@/api/board';
import {imageUpload} from '@/api/main'

export default {
  name:"StudyGroupForm",
  data() {
    return {
      switch1: true,
      title:'',
      description:'',
      file:[],
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
    async onCreate(){
      console.log('사진')
      console.log(this.file)
      console.log('/사진')
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
      try{
        if(this.file.length!=0){
          let fd = new FormData();
          fd.append('file',this.file)
          
          const responseUpload = await imageUpload(fd)
          board.board_image = String(responseUpload.data.imgPath)
        }


        await boardApi.board_create(board).then(response => {
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
          alert('스터디 그룹 생성')
          
          this.$router.push({name:'Study',params:{board_id:response.data.board_id}})
        }).catch(error => {
          console.log(error);
          this.$router.push({name:'SearchBoard'})
          alert("보드 생성에 실패하였습니다.");
        })
      }catch(err){
        this.$router.push({name:'SearchBoard'})
        console.log('BoardForm - 보드생성 에러')
        console.log(err)
      }
    }
  }
}
</script>

<style scoped>
/* 전체 메인 배경색 */
.main-bg-color {
  display: flex;
  flex-direction: column;
  justify-content: center;
  /* align-items: center; */
  max-width: 580px;
  width: 100%;
  margin: 50px auto;
  background-color: var(--basic-color-bg2);
  box-shadow: var(--basic-shadow-s);
  border-radius: 10px;
  padding: 40px 40px;
}
.join-form {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  /* align-items: center; */
  /* height: 40vh; */
  width: 100%;
}
.join-input {
  width: 100%;
  float: left;
  position: relative;
  margin-bottom: 10px;
}
input,
select {
  background: transparent;
  font-size: 1em;
  width: 100%;
  height: 50px;
  line-height: 1em;
  padding: 0 20px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: 0.2s;
  transition: 0.2s;
  border-radius: 0;
  outline: none;
}

input:hover,
input:focus,
select:hover,
select:focus,
input:hover,
input:focus {
  border-bottom: 1px solid #000;
}

.btn-write {
  position: sticky;
  text-align: center;
  margin-top: 30px;
  height: 50px;
  width:100%;
  border: none;
  color: var(--basic-color-fill);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: #ebebe9 !important;
  box-shadow: 10px 10px 20px #bcbcba, 
              -10px -10px 20px #ffffff;
  border-radius: 15px !important;
  transition: 0.3s all ease;
}
.btn-write:hover,
.btn-write:active,
.btn-write:focus {
  color: #ebebe9 !important;    
  background-color: var(--basic-color-key) !important;
}

/* 추가기능 항목 아이템들 */
#add-func-group{
  margin-top:20px;
}
#add-func-item {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 0 20px 0 25px;
}
/* 추가기능 항목 설명 아이콘 */
#add-func-info {
  align-self: flex-end;
}
/* 해쉬태그 나오는 부분 */
.hash-group {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
</style>