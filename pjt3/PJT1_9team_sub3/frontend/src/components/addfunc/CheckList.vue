<template>
  <v-card id="container">
    <div id="title">
      <!-- 체크리스트 제목 -->
      <div>체크 리스트</div>
      <!-- 수정버튼 -->
      <v-icon id="edit-icon"
        large
        @click="checkListEdit"
        v-if="isManager && !isEdit"
      >
        mdi-cog
      </v-icon>
      <!-- 체크리스트 수정 완료 버튼 -->
      <v-icon 
        large
        color="#0B2945"
        class="edit"
        @click="checkListEdit"
        v-if="isManager && isEdit"
      >mdi-circle-edit-outline
      </v-icon>
    </div>
    <!-- 체크리스트 입력 부분 -->
    <div 
      id="input"
      v-if="isManager && isEdit"
    >
      <v-text-field
              id="input-tag"
              v-model="checkListInput"
              label="추가할 체크리스트를 입력해주세요"
              hide-details
              solo
              @keypress.enter="createChecklist()"
      ></v-text-field>
    </div>
    <!-- 입력된 체크리스트 보여줄 부분 -->
    <div>
      <CheckItem 
        v-for="(check, idx) in checkList"
        :key="idx"
        :check="check"
        :is-edit="isEdit"
        @delete-item="deleteItem(check)"
        @change-item-done="changeItemDone(check)"
        @change-item-contents="changeItemContents"
      />
    </div>
    <!-- 체크리스트 편집 완료 -->
    <div class="careful-line"></div>
  </v-card>
</template>

<script>
// API
import * as checkListApi from '@/api/addfunc'
// style 적용
import '@/assets/css/static/style.css';
// 현재 보여줄 체크리스트 항목
import CheckItem from "@/components/addfunc/CheckItem"

export default {
  name: "CheckList",
  components: {
    CheckItem,
  },
  props: {
    isManager: Boolean,
  },
  created() {
    this.getList()
  },
  watch: {
    // 작성되면 이를 확인해서 변화
  },
  computed: {

  },
  data() {
    return {
      // 체크리스트 입력 받는 부분
      checkListInput: '',
      // 현재 보드의 체크리스트 담을 배열
      checkList: [],
      // 편집활성화 flag
      isEdit: false,
    }
  },
  methods: {
    // 관리자가 들어오면 체크리스트 수정가능케
    checkListEdit() {
      // 수정 중인데 수정 중이면 못 바꾸게
      if (this.$store.state.addfunc.isEditNow && this.isEdit) {
        alert('편집을 마치고 종료하세요')
      } else { this.isEdit = !this.isEdit }
    },
    // 생성부분
    createChecklist() {
      // board id 가져오기
      const BOARD_ID = Number(this.$route.params.board_id)
      // param값 만들기
      const checkListItem = {
        board_id:BOARD_ID,
        check_list_item_description: this.checkListInput,
        check_list_item_done: false,
        is_used: 1,
      }
      // checkList 배열에 넣어주기(맨 앞)
      this.checkList.unshift(checkListItem)
      // input 비워주기
      this.checkListInput = ''
      // api 호출하기
      checkListApi.checkListCreate(checkListItem)
        .then(res => {
          // console.log(res)
        })
        .catch(err => {
          // console.error(err)
        })
    },
    // 조회부분
    getList() {
      const BOARD_ID = Number(this.$route.params.board_id)
      // 리스트 가져오기
      checkListApi.getCheckList(BOARD_ID)
        .then(res => {
          // 1. 사용 중인 체크리스트만 나오게(is) + 내림차순 정렬
          this.checkList = res.data.checkList.filter( item => {
            return item.is_used === 1
          }).reverse();
        })
        .catch(err => {
          // console.error(err)
        })
    },
    // 삭제되면 해당 checkList 배열에서 삭제하기
    deleteItem(check) {
      // 1. index 찾기
      const index = this.checkList.findIndex(val => val.check_list_item_id === check.check_list_item_id)
      // 2. 해당 인덱스 제거
      this.checkList.splice(index, 1)
    },
    // 완료 상태 바뀌면 반영
    changeItemDone(check) {
      // 1. index 찾기
      const index = this.checkList.findIndex(val => val.check_list_item_id === check.check_list_item_id)
      // 2. 해당 인덱스 완료상태 변경
      this.checkList[index].check_list_item_done = !this.checkList[index].check_list_item_done
      // 변경 내용 변수에 담아서 API 보내기
      const params = this.checkList[index]
      // API 요청으로 상태 변환
      checkListApi.modifyCheckList(params)
        .then(res => {
          // console.log(res)
        })
        .catch(err => {
          // console.error(err)
        })
    },
    // 내용바뀌면 확인하기
    changeItemContents(itemInfo) {
      // 해당 내용에 맞게 갈아 껴준다
      // 1. index 찾기
      const index = this.checkList.findIndex(val => val.check_list_item_id === itemInfo.check_list_item_id)
      // 2. 내용 변경
      this.checkList[index].check_list_item_description = itemInfo.check_list_item_description
      // DB 변경
      const params = this.checkList[index]
      // API 요청으로 상태 변환
      checkListApi.modifyCheckList(params)
        .then(res => {
          // console.log(res)
          this.ingEditItem = !this.ingEditItem
        })
        .catch(err => {
          // console.error(err)
        })
    },
  },
}
</script>

<style scoped>
/* 전체 폰트 */
#container {
  font-family: 'Noto Sans KR', sans-serif !important;
  box-shadow: var(--basic-shadow-s) !important;
  border-radius: 15px;
  background-color: var(--basic-color-bg2) !important;
  margin: 5% 0;
  padding: 5% 10%;
  min-height: 30vh;
}
/* 제목 */
#title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1em;
}
/* 편집 들어오는 버튼(아이콘) */
#edit-icon {
  cursor:pointer;
  color: #0B2945;
}
#edit-icon:hover {
  color: #AA2610;
}
/* 수정완료버튼 */
.edit:hover {
  cursor: pointer;
  border-radius: 50%;
  background: #0B2945 !important;
  opacity: 0.6;
}
.edit:active {
  background-color: #0B2945;
}
/* 체크리스트 입력 부분 */
#input {
  margin: 0 0 20px 0;
}
</style>