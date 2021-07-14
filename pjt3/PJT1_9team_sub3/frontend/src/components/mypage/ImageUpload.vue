<template>
  <v-dialog v-model="dialog" persistent max-width="400px">
    <template v-slot:activator="{ on, attrs }" class="pa-5">
      <div v-bind="attrs" v-on="on" class="profile-imgae-edit" />
    </template>
    <v-card>
      <form ref="form" @submit.stop.prevent="handleSubmit">
        <b-form-group label-for="multiple-media">
          <b-form-file id="multiple-media" v-model="images" placeholder="Choose a file or drop it here..." browse-text="🖼" @change="onChangeImages">
            <!--card-image-->
            <template slot="file-name" slot-scope="{ names }">
              <b-badge variant="dark">{{ names[0] }}</b-badge>
              <b-badge v-if="names.length > 1" variant="dark" class="ml-1"> + {{ names.length - 1 }} More files </b-badge>
            </template>
          </b-form-file>
        </b-form-group>
        <div v-if="previewImgUrl" class="image-section center">
          <b-img :src="previewImgUrl" style="max-width: 10rem; margin-left:calc(50% - 5rem);"></b-img>
          <b-icon
            class="deleteImg"
            @click="deleteImage"
            icon="x-circle-fill"
            aria-hidden="true"
            style="position:absolute; margin: -8px 0 0 -8px;"
          ></b-icon>
        </div>
      </form>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text @click="dialog = false" class="r-desc footer-btn">
          취소
        </v-btn>
        <v-btn text @click="submit" class="r-desc footer-btn">
          확인
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      // 비밀번호 변경 버튼
      dialog: false,
      images: [],
      previewImgUrl: null,
    };
  },
  methods: {
    submit() {
      if (this.images.length != 0) {
        let formData = new FormData();
        formData.append('file', this.images);
        this.$store.dispatch('auth/actImageUpload', formData).then((result) => {
          if (result) {
            //response로 받은 path를 vuex에 저장
            //Mypage.vue에서 내정보 수정완료 버튼을 누를 경우 함께 전송
            //toast를 통해서 내정보 변경 버튼 눌러 저장하도록 유도
            this.$toast.open({
              message: '수정 버튼을 눌러 내정보 변경을 완료해주세요.',
              type: 'info',
              duration: 5000,
            });
          }
        });
      }
      this.dialog = false;
    },
    // 이미지
    deleteImage() {
      this.previewImgUrl = null;
      this.images = [];
    },
    onChangeImages(e) {
      console.log(e.target.files);
      const file = e.target.files[0];
      this.images.push(file);
      this.previewImgUrl = URL.createObjectURL(file);
    },
  },
};
</script>

<style scoped>
.profile-imgae-edit {
  position: absolute;
  z-index: 10;
  left: 0;
  cursor: pointer;
  width: 100%;
  height: 100%;
  background-color: #000;
  background-image: url(../../assets/images/image_upload_w.png);
  background-size: 30px 30px;
  background-position: center;
  border-radius: 50%;
  opacity: 0.4;
  transition: transform 0.5s, opacity 0.5s ease;
}
.profile-imgae-edit:hover {
  transform: scale(1.1);
  opacity: 0.8;
}
</style>
