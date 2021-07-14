<template>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      persistent
      max-width="600px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-icon 
          id="edit-icon"
          large
          color="primary"
          dark
          v-bind="attrs"
          v-on="on"
        >
          mdi-plus-box
        </v-icon>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">투표 생성</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="question"
                  label="질문"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="tempItem"
                  label="선택지 입력"
                  placeholder="입력후 엔터를 눌러주세요."
                  @keypress.enter="pushItem"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <div id="item-set" v-for="(item, idx) in items" :key="idx">
                  <p id="item">{{item}}</p>
                  <p id="item-delete" @click="deleteItem(idx)">X</p>
                </div>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="reset"
          >
            Close
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="createVote"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import {vote_create,vote_item_create} from '@/api/addfunc'

export default {
  data: () => ({
    question:'',
    tempItem:'',
    items:[],
    dialog: false,
  }),
  methods:{
    pushItem(){
      if(this.tempItem!=''){
        this.items.push(this.tempItem.trim())
        this.tempItem=''
      }
    },
    deleteItem(idx){
      this.items.splice(idx,1)
    },
    reset(){
      this.question=''
      this.tempItem=''
      this.items=[]
      this.dialog=false
    },
    async createVote(){

      let vote ={
        board_id:Number(this.$route.params.board_id),
        vote_name: this.question,
        vote_description:'null',
      }
      try{
        console.log(vote)
        const createResponse = await vote_create(vote)
        console.log(createResponse)
        const vote_id = createResponse.data.vote_id

        this.items.forEach(item =>{
          vote_item_create(Number(vote_id),item)
          .then(res=>{
            console.log(res)
          })
          .catch(err=>{
            console.log('아이템 넣기 실패')
            console.log(err)
          })
        })

        this.$store.dispatch('addfunc/flagWrite')
        this.reset()
      }catch(err){
        console.log(err)
        console.log('투표에러')
      }
    }
  }
}
</script>

<style scoped>
.v-application .justify-center{
  -webkit-box-pack:flex-end !important;
  justify-content: flex-end !important; 
}  


/* 아이템 */
#item-set{
  display: flex;
  justify-content: space-between;
}
#item{
  display: inline-block;
}
#item-delete{
  cursor: pointer;
  color:#aa2610;
}
</style>