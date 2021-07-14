<template>
  <div>
    <!-- <b-button v-b-modal.modal-post variant="light" class="btn-write">글쓰기</b-button> -->
    <div v-b-modal.modal-post>수정</div>
    <!-- <b-modal id="modal-post" title="Info" v-if="!inBoard" ok-only>
      <p class="my-4">구독하시면 글을 작성할 수 있어요😊</p>
      <template #modal-footer="{ok}">
        <b-button variant="submit" @click="ok()">
          오키
        </b-button>
      </template>
    </b-modal> -->
    <b-modal
      id="modal-post"
      ref="modal"
      size="xl"
      title="Modify"
      no-close-on-backdrop
      @show="resetModal"
      @hidden="resetModal"
      @modal-cancel="resetModal"
      @ok="handleOk"
    >
      <form ref="form" @submit.stop.prevent="handleSubmit">
        <!--market에서만 쓰는 지역-제목 form-->
        <div class="form-row" v-if="flagBoard==='Market'">
          <b-form-group
            label-for="location-input"
            invalid-feedback="지역을 선택해주세요"
            :state="locationState"
           class="col-12 col-sm-3 mb-0"
          >
            <b-form-select
              id="location-input"
              v-model="location.selected"
              :options="location.options"
              :state="locationState"
              required
            >
              <template #first>
                <b-form-select-option :value="null" disabled>-- 지역을 선택해주세요 --</b-form-select-option>
              </template>
            </b-form-select>
          </b-form-group>
          <b-form-group
            label-for="title-input"
            invalid-feedback="title is required"
            :state="titleState"
            class="col-sm-9 col-12"
          >
            <b-form-input
              id="title-input"
              placeholder="제목"
              v-model="tempTitle"
              :state="titleState"
              required
            ></b-form-input>
          </b-form-group>
        </div>
        <!--범용적으로 쓰는 제목 form-->
        <b-form-group
          label-for="title-input"
          invalid-feedback="title is required"
          :state="titleState"
          v-if="flagBoard==='others'"
        >
          <b-form-input
            id="title-input"
            placeholder="제목"
            v-model="tempTitle"
            :state="titleState"
            required
          ></b-form-input>
        </b-form-group>
        <b-form-group
          label-for="description-input"
          invalid-feedback="description is required"
          :state="descriptionState"
        >
          <b-form-textarea
            id="description-input"
            placeholder="무슨생각하고 있음?"
            style="height:300px"
            v-model="tempDescription"
            :state="descriptionState"
            required
            no-resize
          ></b-form-textarea>
        </b-form-group>
        <b-form-group
          label-for="multiple-media"
          disabled
        >
          <b-form-file 
            id="multiple-media"
            v-model="tempImages"
            placeholder="Choose a file or drop it here..."
            browse-text='🖼'
            @change="onChangeImages"
          >
          <!--card-image-->
            <template slot="file-name" slot-scope="{ names }">
              <b-badge variant="dark">{{ names[0] }}</b-badge>
              <b-badge v-if="names.length > 1" variant="dark" class="ml-1">
                + {{ names.length - 1 }} More files
              </b-badge>
            </template>
          </b-form-file>
        </b-form-group>
        <div v-if="tempPreviewImgUrl" class="image-section">
          <b-img
              :src="tempPreviewImgUrl"
              style="max-width: 10rem;"
          ></b-img>
          <b-icon class="deleteImg" @click="deleteImage" icon="x-circle-fill" aria-hidden="true"></b-icon>
        </div>
      </form>
      <template #modal-footer="{ok, cancel}">
        <!-- Emulate built in modal footer ok and cancel button actions -->
        <b-button variant="submit" @click="ok()">
          Edit
        </b-button>
        <b-button variant="cancel" @click="cancel()">
          Cancel
        </b-button>
      </template>
    </b-modal>
    <!-- <b-modal
      id="modal-post"
      ref="modal"
      size="xl"
      title="Post"
      no-close-on-backdrop
      ok-only
      @show="resetModal"
      @hidden="resetModal"
      @ok="handleOk"
    >
      <form ref="form" @submit.stop.prevent="handleSubmit">
        여기?
      </form>
      <template #modal-footer="{ok}">
        <b-button variant="submit" @click="ok()">
          게시
        </b-button>
      </template>
    </b-modal> -->
  </div>
</template>

<script>
import * as postApi from '@/api/post';
import deepClone from '@/plugins/deepClone'
import {imageUpload,imageDelete} from '@/api/main';

export default {
  name:'ModifyForm',
  props:{
    post:Object,
  },
  data() {
    return {
      tempTitle: '',
      tempDescription:'',
      tempImages:[],
      tempPreviewImgUrl:null,
      
      titleState: null,
      descriptionState: null,
      locationState:null,

      location:{
        selected:null,
        options:[
          {value:'전체',text:'전체'},
          {value:'서울',text:'서울'},
          {value:'대전',text:'대전'},
          {value:'광주',text:'광주'},
          {value:'구미',text:'구미'},
        ]
      },
    }
  },
  computed:{
    flagBoard() {
      const boardName = this.$route.name
      if(boardName==="MarketPost"){
        return "Market"
      }else if(boardName==="learnSharePost"){
        return "learnShare"
      }else if(boardName){
        return "others"
      }
      return ''
    }
  },
  methods: {
    deleteImage(){
      this.tempPreviewImgUrl = null
      this.tempImages=[]
    },
    onChangeImages(e) {
        const file = e.target.files[0];
        this.tempImages.push(file)
        this.tempPreviewImgUrl = URL.createObjectURL(file);
    },
    titleCheckFormValidity() {
      const valid = this.$refs.form.checkValidity()
      this.titleState = valid
      return valid
    },
    descriptionCheckFormValidity() {
      const valid = this.$refs.form.checkValidity()
      this.descriptionState = valid
      return valid
    },
    resetModal() {
      this.tempTitle = this.post.post_title
      this.tempDescription = this.post.post_description
      this.location.selected = this.post.post_header
      this.tempPreviewImgUrl = this.post.post_image

      this.titleState = null
      this.descriptionState = null
      this.locationState = null
      this.tempImages=[]
    },
    handleOk(bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault()
      // Trigger submit handler
      this.handleSubmit()
    },
    async handleSubmit() {
      // Exit when the form isn't valid
      if (!this.titleCheckFormValidity() ) {
        return
      }
      if (!this.descriptionCheckFormValidity()) {
        return
      }
      // Push the name to submitted names

      const curationName = this.$route.name
      let BOARD_ID
      if(curationName!="Post"){
        if(curationName==="MarketPost"){
          BOARD_ID = this.$store.state.curationId["Market"]
        }else if(curationName==="LearnSharePost"){
          BOARD_ID = this.$store.state.curationId["LearnShare"]
        }
      }else{
        BOARD_ID = Number(this.$route.params.board_id)
      }

      let postItem = deepClone(this.post)
      postItem.post_title = this.tempTitle
      postItem.post_description = this.tempDescription
      postItem.post_header = this.location.selected

      
      try{
        console.log(postItem)
        // 이미지 a -> 이미지 b로 바꾸기
        if((postItem.post_image!='' && postItem.post_image != this.tempPreviewImgUrl) || this.tempPreviewImgUrl===null){
          await imageDelete(postItem.post_image)
          .then(res=>{
            console.log('이미지 삭제 완료!')

          })
          .catch(err=>{
            console.log(err)
          })
        }

        if(this.tempImages.length!=0){
          let fd = new FormData();
          fd.append('file',this.tempImages)
          
          const responseUpload = await imageUpload(fd)
          postItem.post_image = String(responseUpload.data.imgPath)
        }
        else if(postItem.post_image!=this.tempPreviewImgUrl || this.tempPreviewImgUrl===null){
          postItem.post_image = ''
        }
          
        // }

        const login_id = localStorage.userId
        await postApi.modify({postItem,login_id})
          .then(res=>{
            console.log('post 편집')
            this.$store.dispatch('post/isModifyFlag')
          })
          .catch(err=>{
            
            console.log(`post 편집 실패`)
          })

      }catch(err){
        console.log('에러발생')
        console.log(err)
      }
      
      this.$nextTick(() => {
        this.$bvModal.hide('modal-post')
      })
    }
  }
}
</script>

<style scoped>
.image-section{
  display: flex;
  /* align-items: baseline; */
}
.deleteImg{
  transform: translate(-5px,0);
  cursor: pointer;
}
.modal-footer{
  display: flex;
}

.btn-submit {
  /* width: 100%; */
  height: 40px;
  /* margin: 5px 10px; */
  font-size: 20px;
  border: solid 1px #000;
  transition: color 0.3s, background-color 0.3s ease;
}
.btn-submit:hover,
.btn-submit:active {
  background-color: #000 !important;
  color: #fff;
}
.btn-cancel {
  /* width: 100%; */
  height: 40px;
  /* margin: 5px 10px; */
  font-size: 20px;
  border: solid 1px #000;
  transition: color 0.3s, background-color 0.3s ease;
}
.btn-cancel:hover,
.btn-cancel:active {
  background-color: #aa2610 !important;
  border: solid 1px #aa2610;
  color: #fff;
}
/* @media screen and (max-width:576px) {
  .btn-submit{
    background-color: #000 !important;
    color: #fff;
  }
} */
</style>