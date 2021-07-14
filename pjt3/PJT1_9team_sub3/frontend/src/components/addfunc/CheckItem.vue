<template>
  <div 
    class="container"
  >
    <!-- 완료시 보여줄 부분 -->
    <v-icon 
      v-if="check.check_list_item_done"
      color="#AA2610"
      @click="changeItemDone"
      :class="{ justMember : !isEdit}"
    >mdi-sticker-check</v-icon>
    <!-- 완료 X일 경우 -->
    <v-icon 
      v-if="!check.check_list_item_done"
      @click="changeItemDone"
      :class="{ justMember : !isEdit}"
    >mdi-sticker-outline</v-icon>
    <!-- 체크리스트 내용 -->
    <div 
      class="contents"
    >
      <!-- 실제 체크리스트 내용 -->
      <div 
        v-if="!isModify"
        @click="modifyContents"
        :class="[{ justMember : !isEdit}, { finishItem : check.check_list_item_done }]"
      >
        {{ check.check_list_item_description }}
      </div>
      <!-- 수정할 값 -->
      <div 
        id="input"
        v-if="isModify && isEdit"
      >
        <v-text-field
          solo
          dense
          hide-details
          block
          v-model="tempInput"
          @keypress.enter="modifyCheckItem()"
        >
        </v-text-field>
        <v-icon 
          color="#0B2945"
          class="edit"
          @click="modifyCheckItem()"
        >mdi-circle-edit-outline
        </v-icon>
      </div>
      <!-- 삭제버튼 -->
      <v-icon 
        color="#aa2610" 
        class="delete"
        v-if="!isModify && isEdit"
        @click="deleteCheckItem()"
      >mdi-close</v-icon>
    </div>   
  </div>

</template>

<script>
// API
import * as checkListApi from '@/api/addfunc'
// deep copy
import deepClone from '@/plugins/deepClone'

export default {
  name: "CheckItem",
  props: {
    check: Object,
    // 편집 활성화
    isEdit: Boolean,
  },
  created() {
    // 변화 체크
    // this.modifyContents()
  },
  computed() {
    // 수정 중에는 체크리스트 편집을 마칠 수 없다

  },
  data() {
    return {
      // 
      // 수정 시 temp값 - 입력 중단시 그 전값 넣어줘야 함 
      tempInput: this.check.check_list_item_description,
      isModify: false,
    }
  },
  methods: {
    // input - text 변환
    modifyContents() {
      if (this.isEdit) {
        this.isModify = !this.isModify
        // 수정 중에는 완료할 수 없게 데이터를 부모에게
        if (this.isModify) {
          this.$store.dispatch('addfunc/isEditCheckList', true)
        } else {
          this.$store.dispatch('addfunc/isEditCheckList', false)
        }
      }
    },
    // 삭제하기
    deleteCheckItem() {
      // params 형태에 맞게 담아주고
      const params = {check_list_item_id: this.check.check_list_item_id} 
      // emit으로 부모에서 지워주기
      this.$emit('delete-item')
      checkListApi.deleteCheckList(params
      )
        .then(res => {
          // console.log(res)
        })
        .catch(err => {
          // console.error(err)
        })
    },
    // 수정하기 - 1 (완료상태 변경)
    changeItemDone() {
      // 조건으로 일반 멤버는 변경 불가능하게
      if (this.isEdit) {
      // emit으로 부모 상태 변환(보여주는 부분)
      this.$emit('change-item-done')}
    },
    // 수정하기 - 2 (내용변경)
    modifyCheckItem() {
      if (this.isEdit) { 
        // 빈 값 들어왔을 때 처리
        if (this.tempInput.trim() === '') {
          alert('공백입니다!')
        } else {
        // 정보 넣어주기
        const itemInfo = {
          check_list_item_id: this.check.check_list_item_id,
          check_list_item_description: this.tempInput.trim()
        }
        // 부모로 이벤트 보내기
        this.$emit('change-item-contents', itemInfo)
        // 완료 후 edit상태 변경
        this.modifyContents()
        }
      }
    },
  }
}
</script>

<style scoped>
.container {
  margin: 12px 0;
  padding: 0 12px;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
}
/* hover시 삭제버튼 보이게 */
.contents {
  flex-basis: 100%;
  display: flex;
  justify-content: space-between;
  cursor: pointer;
}
.delete:hover {
  cursor: pointer;
  border-radius: 50%;
  border: 1px solid #aa2610;
  opacity: 0.6;
}
.delete:active {
  background-color: #aa2610;
}
/* 체크리스트 수정값 입력 */
#input {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex-basis: 100%;
}
/* 수정버튼 */
.edit:hover {
  cursor: pointer;
  border-radius: 50%;
  background: #0B2945;
  opacity: 0.6;
}
.edit:active {
  background-color: #0B2945;
}
/* 편집 아닐때 커서 활성화 안되게 */
.justMember {
  cursor: default !important;
}
/* 완료된 체크리스트는 연하게 */
.finishItem {
  font-weight: 100;
  color: #695c4c;
}
</style>