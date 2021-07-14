<template>
  <v-app class="back-gd">
  <div id="app" class="main-bg-color">
    <!-- 전체 포함하는 컨테이너 -->
    <div class="join-form">
      <!-- <div 
        class="text-overline text-center font-weight-black mb-3"
        style="font-size: 24px !important;"
      >보드 생성하기
      </div> -->
      <h2 class="mb-3">보드 생성하기</h2>
      <div class="join-input">
        <!-- 보드 생성하기 제목 -->
        <div>
            <!-- 보드 이름 -->
            <v-text-field label=" 보드이름" v-model="title" prepend-icon="T"></v-text-field>
            <!-- 보드 설명 -->
            <v-textarea label=" 상세설명" rows="4"
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
            <!-- 익명 여부 -->
            <!-- <v-col>
              <div class="my-2">익명성 여부</div>
              <b-form-checkbox v-model="options.ikmyeong" name="check-button" switch>
                <p v-if="!options.ikmyeong" style="margin:0; text-decoration:line-through;">익명 보드</p>
                <b v-if="options.ikmyeong">익명 보드</b>
              </b-form-checkbox>
            </v-col> -->
            <!-- 지역 정보 -->
            <!-- <v-col>
              <v-select
                v-model="location"
                :items="options.location"
                label="지역"
                solo
              ></v-select>
            </v-col> -->
            <!-- 추가 기능 -->
              <!-- 추가기능 추가 정보(편집) -->
              <div class="add-func-group">
                <div>추가기능</div>
                <div
                  id="add-func-item"
                  v-for="(func, idx) in addFuncAll"
                  :key="idx"
                >
                  <v-checkbox
                    hide-details
                    color="#0B2945"
                    :label="func.option"
                    v-model="func.state"
                  ></v-checkbox>
                  <v-tooltip right>
                    <template v-slot:activator="{ on, attrs }">
                      <v-icon
                        id="add-func-info"
                        v-bind="attrs"
                        v-on="on"
                        color="#AA2610"
                      >mdi-information-outline</v-icon>
                    </template>
                    <span v-html="func.explain"></span>
                  </v-tooltip>
                </div>
              </div>
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
  </v-app>
</template>

<script>
import * as boardApi from '@/api/board';
import {imageUpload} from '@/api/main'

export default {
  name:"BoardForm",
  watch: {
    addFuncAll:'checkAddFunc'
  },
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
        location: ['서울', '대전', '구미', '광주'],
        ikmyeong:false,
      },
      // 전체 추가기능 목록 (체크되어 있는지)
      addFuncAll: [
        {
          option: '체크리스트',
          state: false,
          explain: '구성원들과 함께 간단한 <strong>할 일 목록</strong>을 만들어서 관리해보세요.<br>보드를 효율적으로 사용할 수 있게 됩니다.',
        }, 
        {
          option: '캘린더',
          state: false,
          explain: '캘린더에 일정을 표시하여 서로의 일정을 공유하고<br> 구성원들의 <strong>스케쥴 관리를 효율적</strong>으로 할 수 있게 도와줍니다.',
        }, 
        {
          option: '투표',
          state: false,
          explain: '결정하기 힘든 일은 투표를 통해 확인하는 것은 어떨까요?<br>투표를 만들고 투표내 항목을 만들어 투표를 생성해보세요',
        }, 
        {
          option: '랭킹',
          state: false,
          explain: '보드를 활발히 활동하는 유저는 누군지 확인할 수 있게<br> <strong>TOP3</strong> 활동 유저를 확인해보세요',
        }, 
      ],
    }
  },
  methods: {
    // 추가기능 test
    checkAddFunc() {
      console.log(this.addFuncAll)
    },
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
        board_igmyeong: 0,
        board_image:'',
        board_hash:this.hashtags.join('|'),
// 추가기능 삽입 추가
        checklist_flag:(this.addFuncAll[0].state ? 1 : 0),
        calendar_flag:(this.addFuncAll[1].state ? 1 : 0),
        // // 랭킹 현재 연결 없으니 주석처리
        user_rank_flag:(this.addFuncAll[3].state ? 1 : 0),
        vote_flag:(this.addFuncAll[2].state ? 1 : 0),
        board_state:0
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
          alert('게시판 보드 생성')
          
          this.$router.push({name:'Board',params:{board_id:Number(response.data.board_id)}})
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
/* 겉 바탕 */
.back-gd {
  background-color: var(--basic-color-bg);
}
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
.add-func-group{
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