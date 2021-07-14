<template>
  <div>
    <b-button v-b-modal.modal-post class="btn-write newmorphism">{{writeName}}</b-button>

    <!-- 구독한 사람만 쓸 수 있다고 알림 -->
    <b-modal id="modal-post" title="Info" v-if="!inBoard" ok-only>
      <p class="my-4">구독하시면 글을 작성할 수 있어요😊</p>
      <template #modal-footer="{ok}">
        <b-button variant="submit" @click="ok()">
          오키
        </b-button>
      </template>
    </b-modal>
    <!-- 글쓰기 form-->
    <b-modal
      id="modal-post"
      ref="modal"
      size="xl"
      title="글쓰기"
      no-close-on-backdrop
      ok-only
      @show="resetModal"
      @hidden="resetModal"
      @ok="handleOk"
      v-if="inBoard"
    >
      <form ref="form" @submit.stop.prevent="handleSubmit">
        <!--학습공유에서만 쓰는 헤더-->
        <div class="form-row" v-if="flagBoard==='LearnShare'">
          <b-form-group label="" label-for="tags-with-dropdown" style="width:100%" class="mb-0">
            <b-form-tags id="tags-with-dropdown" v-model="learnshare.value" no-outer-focus class="mb-2">
              <template v-slot="{ tags, disabled, addTag, removeTag }">
                <ul v-if="tags.length > 0" class="list-inline d-inline-block mb-2">
                  <li v-for="tag in tags" :key="tag" class="list-inline-item">
                    <b-form-tag
                      @remove="removeTag(tag)"
                      :title="tag"
                      :disabled="disabled"
                      variant="info"
                      style="background:#0B2945"
                    >{{ tag }}</b-form-tag>
                  </li>
                </ul>

                <b-dropdown id="skillset" size="sm" variant="outline-secondary" block menu-class="w-100">
                  <template #button-content id="choose-skills">
                    <b-icon icon="tag-fill"></b-icon> Choose Skills
                  </template>
                  <b-dropdown-form @submit.stop.prevent="() => {}">
                    <b-form-group
                      label="Search"
                      label-for="tag-search-input"
                      label-cols-md="auto"
                      class="mb-0"
                      label-size="sm"
                      :description="searchDesc"
                      :disabled="disabled"
                    >
                      <b-form-input
                        v-model="learnshare.search"
                        id="tag-search-input"
                        type="search"
                        size="sm"
                        autocomplete="off"
                      ></b-form-input>
                    </b-form-group>
                  </b-dropdown-form>
                  <b-dropdown-divider></b-dropdown-divider>
                  <b-dropdown-item-button
                    v-for="option in availableOptions"
                    :key="option"
                    @click="onOptionClick({ option, addTag })"
                    
                  >
                    {{ option }}
                  </b-dropdown-item-button>
                  <b-dropdown-text v-if="availableOptions.length === 0">
                    There are no tags available to select
                  </b-dropdown-text>
                </b-dropdown>
              </template>
            </b-form-tags>
          </b-form-group>
          <!-- 제목 -->
          <b-form-group
            label-for="title-input"
            invalid-feedback="title is required"
            :state="titleState"
            class="col-12"
          >
            <b-form-input
              id="title-input"
              placeholder="제목"
              v-model="title"
              :state="titleState"
              required
            ></b-form-input>
          </b-form-group>
        </div>
        <!-- 마켓에서만 쓰는 지역-제목 form-->
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
              v-model="title"
              :state="titleState"
              required
            ></b-form-input>
          </b-form-group>
        </div>
        <!-- 범용적인 제목 form-->
        <div class="form-row" v-if="flagBoard==='Others'">
          <b-form-group
            label-for="title-input"
            invalid-feedback="title is required"
            :state="titleState"
            class="col-12"
          >
            <b-form-input
              id="title-input"
              placeholder="제목"
              v-model="title"
              :state="titleState"
              required
            ></b-form-input>
          </b-form-group>
        </div>

        <!-- 공통 form -->
        <b-form-group
          label-for="description-input"
          invalid-feedback="description is required"
          :state="descriptionState"
        >
          <b-form-textarea
            id="description-input"
            placeholder="무슨생각하고 있음?"
            style="height:300px"
            v-model="description"
            :state="descriptionState"
            required
            no-resize
          ></b-form-textarea>
        </b-form-group>
        <b-form-group
          label-for="multiple-media"
        >
          <b-form-file 
            id="multiple-media"
            v-model="images"
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
        <div v-if="previewImgUrl" class="image-section">
          <b-img
              :src="previewImgUrl"
              style="max-width: 10rem;"
          ></b-img>
          <b-icon class="deleteImg" @click="deleteImage" icon="x-circle-fill" aria-hidden="true"></b-icon>
        </div>
      </form>
      <template #modal-footer="{ok}">
        <!-- Emulate built in modal footer ok and cancel button actions -->
        <b-button variant="submit" @click="ok()">
          게시
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
import {imageUpload} from '@/api/main'

export default {
  name:'PostWrite',
  data() {
    return {
      title: '',
      description:'',
      header:'전체',
      state:0,
      images:[],
      previewImgUrl:null,
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
      learnshare:{
        value: [],
        search:'',
        options:[
          'HTML','Java','JavaScript','PHP','Python','ES6','CSS','C++','Ruby',
          'MySQL','PostreSQL','MongoDB','MariaDB','React','JQuery','Vue.js','AngularJS',
          'Node.js','SpringBoot','Spring','Django',
          'AmazonS3','AmazonEC2',
          'Git','Docker','Postman','NGINX','Jira','Ubuntu','Jenkins','Redis','Firebase','ApacheTomcat'
        ]
      },
      koreanoptions:[
        {korean:'에이치티엠엘',en:'HTML'},{korean:'자바',en:'Java'},{korean:'자바스크립트',en:'JavaScript'},{korean:'피에이치피',en:'PHP'},{korean:'파이썬',en:'Python'},{korean:'씨에스에스',en:'CSS'},
        {korean:'씨쁠쁠',en:'C++'},{korean:'루비',en:'Ruby'},{korean:'마이에스큐엘',en:'MySQL'},{korean:'몽고디비',en:'MongoDB'},{korean:'마리아디비',en:'MariaDB'},{korean:'포스트그레스큐엘',en:'PostgreSQL'},
        {korean:'리엑트',en:'React'},{korean:'제이쿼리',en:'JQuery'},{korean:'뷰제이에스',en:'Vue.js'},{korean:'앵귤러제이에스',en:'AngularJS'},{korean:'노드제이에스',en:'Node.js'},{korean:'스프링부트',en:'SpringBoot'},
        {korean:'스프링',en:'Spring'},{korean:'장고',en:'Django'},{korean:'아마존에쓰3',en:'AmazonS3'},{korean:'아마존이씨투',en:'AmazonEC2'},{korean:'깃',en:'Git'},{korean:'도커',en:'Docker'},
        {korean:'포스트맨',en:'Postman'},{korean:'엔진엑스',en:'NGINX'},{korean:'지라',en:'Jira'},{korean:'우분투',en:'Ubuntu'},{korean:'젠킨스',en:'Jenkins'},{korean:'레디스',en:'Redis'},
        {korean:'파이어베이스',en:'Firebase'},{korean:'아파치톰캣',en:'ApacheTomcat'}
      ]
        // 에이치티엠엘:'HTML',자바:'Java',자바스크립트:'JavaScript',피에이치피:'PHP',파이썬:'Python',씨에스에스:'CSS',씨쁠쁠:'C++',루비:'Ruby',
        // 마이에스큐엘:'MySQL',몽고디비:'MongoDB',마리아디비:'MariaDB',포스트그레스큐엘:'PostgreSQL',
        // 리엑트:'React',제이쿼리:'JQuery',뷰제이에스:'Vue.js',앵귤러제이에스:'AngularJS',
        // 노드제이에스:'Node.js',스프링부트:'SpringBoot',스프링:'Spring',장고:'Django',
        // 아마존에쓰3:'AmazonS3',아마존이씨투:'AmazonEC2',
        // 깃:'Git',도커:'Docker',포스트맨:'Postman',엔진엑스:'NGINX',지라:'Jira',우분투:'Ubuntu',젠킨스:'Jenkins',레디스:'Redis',파이어베이스:'Firebase',아파치톰캣:'ApacheTomcat'
      
    }
  },
  props:{
    inBoard:Boolean
  },
  computed:{
    flagBoard() {
      const boardName = this.$route.name
      if(boardName==="Market"){
        return "Market"
      }else if(boardName==="LearnShare"){
        return "LearnShare"
      }else if(boardName){
        return "Others"
      }
      return ''
    },
    writeName(){
      const what = this.$route.name
      if(what==='StudyMain'){
        return '스터디홍보'
      }else{
        return '글쓰기'
      }
    },

    // 학습공유 스킬 form 관련
    criteria() {
      // Compute the search criteria
      return this.learnshare.search.trim().toLowerCase()
    },
    availableOptions() {
      const criteria = this.criteria
      // Filter out already selected options
      const options = this.learnshare.options.filter(opt => this.learnshare.value.indexOf(opt) === -1)

      //한글
      const koreanoptions = this.koreanoptions.filter(opt => this.learnshare.value.indexOf(opt.korean) === -1)

      if (criteria) {
        const korean = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
        if(korean.test(criteria)){
          const result = koreanoptions.filter(opt => opt.korean.toLowerCase().indexOf(criteria) > -1);
          
          return result.map(r => r.en)
        }
        // Show only options that match criteria
        return options.filter(opt => opt.toLowerCase().indexOf(criteria) > -1);
      }
      // Show all options available
      return options
    },
    searchDesc() {
      if (this.learnshare.criteria && this.availableOptions.length === 0) {
        return 'There are no tags matching your search criteria'
      }
      return ''
      }
  },
  methods: {
    // 학습공유 스킬 form 관련
    onOptionClick({ option, addTag }) {
      addTag(option)
      this.search = ''
    }
    ,
    // 이미지
    deleteImage(){
      this.previewImgUrl = null
      this.images=[]
    },
    onChangeImages(e) {
        console.log(e.target.files)
        const file = e.target.files[0];
        this.images.push(file)
        this.previewImgUrl = URL.createObjectURL(file);
    },
    titleCheckFormValidity() {
      const valid = this.title.length >1 && this.title.length <28 ? true : false
      this.titleState = valid
      return valid
    },
    descriptionCheckFormValidity() {
      const valid = this.description.length > 1 && this.description.length < 1000 ? true : false
      this.descriptionState = valid
      return valid
    },
    // locationCheckFormValidity() {
    //   const valid = this.header!='' ? true : false
    //   this.locationState = valid
    //   return valid
    // },
    resetModal() {
      this.title = ''
      this.description = ''
      this.header = '전체'
      this.state = 0
      this.titleState = null
      this.descriptionState = null
      
      this.locationState = null
      this.location.selected = null

      this.learnshare.value=[],
      this.learnshare.search=''


      this.images=[]
      this.previewImgUrl=null
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
      // if (!this.locationCheckFormValidity()) {
      //   return
      // }
      // Push the name to submitted names
      const curationName = this.$route.name
      let BOARD_ID
      if(curationName!="Board" && curationName!="Study"){
        BOARD_ID = this.$store.state.curationId[curationName]
      }else{
        BOARD_ID = Number(this.$route.params.board_id)
      }

      // 재사용을 위해 들어오는 데이터에 따라
      // 중고장터의 경우 header에 지역을 입력
      if(this.location.selected!=null){
        this.header = this.location.selected
      }
      // 학습공유의 경우 header에 기술스택을 입력
      if(this.learnshare.value.length!=0){
        this.header = this.learnshare.value.join('|')
      }
      if(this.learnshare.value.length===0 && curationName==='LearnShare'){
        this.header='null'
      }



      let postItem ={
        user_id:String(localStorage.getItem('userId')),
        board_id:BOARD_ID,
        post_title:this.title, 
        post_description:this.description,
        post_image:'',
        post_iframe:'',
        post_header:this.header,
        post_state:0
      }
      try{
        if(this.images.length!=0){
          let fd = new FormData();
          fd.append('file',this.images)
          
          const responseUpload = await imageUpload(fd)
          console.log(responseUpload)
          postItem.post_image = String(responseUpload.data.imgPath)
        }

        await postApi.create(postItem)
          .then(res=>{
            
            // this.$store.dispatch('board/isWriteFlag')
            const curationName = this.$route.name
            let BOARD_ID
            if(curationName==="Board" || curationName==="Study"){
              
              BOARD_ID = Number(this.$route.params.board_id)
            }else{
              BOARD_ID = this.$store.state.curationId[curationName]

            }
            const EACH_LEN = 1
            postApi.getPostList({board_id:BOARD_ID, user_id:localStorage.userId,page:0,size:EACH_LEN})
            .then(res=>{
              const newPostId = res.data.postList[0].post_id

              let returnName
              if(curationName==='Market'){
                returnName = 'MarketPost'
                this.$router.push({ name: returnName, params: { post_id: newPostId } });
              }else if(curationName ==='LearnShare'){
                returnName = 'LearnSharePost'
                this.$router.push({ name: returnName, params: { post_id: newPostId } });
              }else if(curationName ==='StudyMain'){
                returnName = 'StudyMainPost'
                this.$router.push({ name: returnName, params: { post_id: newPostId } });
              }else if(curationName ==='Recruitment'){
                returnName = 'RecruitmentPost'
                this.$router.push({ name: returnName, params: { post_id: newPostId } });
              }else{
                if(curationName==='Study'){
                  returnName = 'StudyGroupPost'
                }else{
                  returnName = 'Post'
                }
                this.$router.push({ name: returnName, params: { board_id:BOARD_ID,post_id: newPostId } });
              }
            })
            
            
          })
          .catch(err=>{
            console.log(`post 생성 실패 ${err}`)
          })        
      }catch(err){
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
/* 글쓰기 (모달-진입)버튼 */
.btn-write {
  position: sticky;
  text-align: center;
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
/* .btn-write {
  color:var(--basic-color-fill3) !important;
  text-align: center;
  height: 50px;
  width:100%;
  font-weight: 700;
  border: none;
  border-radius: 30px;
  transition: all 0.6s ease !important;
}
.btn-write:hover,
.btn-write:active,
.btn-write:focus {
  background: #ebebe9;
  background: linear-gradient(145deg, #d4d4d2, #fbfbf9);
  box-shadow: 10px 10px 20px #b3b3b1, -10px -10px 20px #ffffff;
} */
.btn-submit {
  width: 100%;
  height: 40px;
  
  font-size: 20px;
  transition: color 0.3s, background-color 0.3s ease;
}
.btn-submit:hover,
.btn-submit:active {
  background-color: #000 !important;
  color: #fff;
}
@media screen and (max-width:576px) {
  .btn-submit{
    width: 100%;
    background-color: #000 !important;
    color: #fff;
  }
}
#skillset /deep/ .dropdown-menu {
  max-height: 200px;
  overflow-y: auto;
}

.dropdown-toggle:hover{
  color:#000 !important;
}
</style>