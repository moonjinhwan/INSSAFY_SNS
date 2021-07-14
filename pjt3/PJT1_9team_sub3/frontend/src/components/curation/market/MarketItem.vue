<template>
  <v-card
    class="mx-auto"
    outlined
  >
    <v-list-item three-line @click="goToDetail">
      <v-list-item-content>
        <v-list-item-title class="text-body-1 mb-1">
          {{ postDto.post_title }}
        </v-list-item-title>
        <v-list-item-subtitle>
          {{ postDto.post_description }}
        </v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-avatar
        tile
        size="80"
      >
        <img
          alt="Avatar"
          :src="image"
          v-if="image!=''"
        >
      </v-list-item-avatar>
    </v-list-item>
  
    <v-card-actions>
      <v-btn
        outlined
        rounded
        text
        block
      >
        {{isState}}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import * as postApi from '@/api/post'
export default {
  name: "MarketItem",
  props:{
    likeId:Number
  },
  data() {
    return {
      // 판매정보 가져올 부분
      title: '(제목)가로본능 팔아요',
      contents: '(내용)미개봉 완제품',
      img: 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460',
      status: '',
      // 판매중, 판매완료, 거래중 상태 표시
      optionStatus: ['판매중', '판매완료', '거래중'],
      postDto:{}
    }
  },
  computed:{
    isState(){
      if(this.postDto.post_state===0){
        return '판매중'
      }else if(this.postDto.post_state===-1){
        return '예약중'
      }else{
        return '판매완료'
      }
    },
    image(){
      return this.postDto.post_image
    }
  },
  created(){
    this.getDetail()
  },
  methods:{
    getDetail(){
      postApi.getPost({
        login_id:String(localStorage.getItem('userId')),
        post_id:Number(this.likeId)
      })
      .then(res=>{
        this.postDto = res.data.postDto
      })
    },
    goToDetail(){
      this.$router.push({name:'MarketPost',params:{post_id:this.postDto.post_id}});
    }
  }
}
</script>

<style scoped>
</style>