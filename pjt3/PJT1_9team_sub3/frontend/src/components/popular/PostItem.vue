<template>
  <div id="wrap">
    <div id="header">
      <div id="pi0" :class="{ aheader: active == 0 }">
        <div id="pi-bg1" class="pi-bg" :class="{ none: !image[0] }" :style="{ backgroundImage: `url(${image[0]})` }" />
        <GradientGenerator class="pi-bg" :class="{ none: image[0] }" />
        <div class="pi-order b-title">1st</div>
        <div class="pi-count t-desc-e">{{ count[0] }}</div>
      </div>
      <div id="pi1" :class="{ aheader: active == 1 }">
        <div id="pi-bg2" class="pi-bg" :class="{ none: !image[1] }" :style="{ backgroundImage: `url(${image[1]})` }" />
        <GradientGenerator class="pi-bg" :class="{ none: image[1] }" />
        <div class="pi-order b-title">2nd</div>
        <div class="pi-count t-desc-e">{{ count[1] }}</div>
      </div>
      <div id="pi2" :class="{ aheader: active == 2 }">
        <div id="pi-bg3" class="pi-bg" :class="{ none: !image[2] }" :style="{ backgroundImage: `url(${image[2]})` }" />
        <GradientGenerator class="pi-bg" :class="{ none: image[2] }" />
        <div class="pi-order b-title">3rd</div>
        <div class="pi-count t-desc-e">{{ count[2] }}</div>
      </div>
    </div>
    <div id="title">
      <div id="pi-p-container">
        <p class="pi-p l-desc" :class="{ atitle: active == 0 }" @click="clickTitle0">{{ title[0] }}</p>
        <p class="pi-p l-desc" :class="{ atitle: active == 1 }" @click="clickTitle1">{{ title[1] }}</p>
        <p class="pi-p l-desc" :class="{ atitle: active == 2 }" @click="clickTitle2">{{ title[2] }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import GradientGenerator from '../etc/GradientGenerator.vue';
export default {
  components: { GradientGenerator },
  name: 'PostItme',
  props: {
    type: String,
    items: Array,
  },
  data() {
    return {
      active: 0,
      count: [0, 0, 0],
      title: ['', '', ''],
      image: [false, false, false],
    };
  },
  created() {
    //null 우회를 위한 local 변수화
    for (let i = 0; i < this.items.length; i++) {
      if (this.items[i] != null) {
        //타이틀 설정
        this.title[i] = this.items[i].post_title;

        if (this.type === 'like') {
          if (this.items[i].post_image != null && this.items.post_image != '' && this.items.post_image != 'null') {
            this.image[i] = this.items[i].post_image;
          }
        } else {
          if (this.items[i].post_img != null && this.items.post_img != '' && this.items.post_img != 'null') {
            this.image[i] = this.items[i].post_img;
          }
        }
        //type 별 수치
        if (this.type === 'like') {
          this.count[i] = this.items[i].post_like;
        } else {
          this.count[i] = this.items[i].comment_count;
        }
      }
    }
  },
  mounted() {
    //1초마다 반전시킴
    setInterval(() => {
      this.active = (this.active + 1) % 3;
    }, 2000);
  },
  methods: {
    clickTitle0: function() {
      this.$router.push(`/board/${this.items[0].board_id}/post/${this.items[0].post_id}`);
    },
    clickTitle1: function() {
      this.$router.push(`/board/${this.items[1].board_id}/post/${this.items[1].post_id}`);
    },
    clickTitle2: function() {
      this.$router.push(`/board/${this.items[2].board_id}/post/${this.items[2].post_id}`);
    },
  },
};
</script>

<style scoped>
.none {
  display: none !important;
}
#wrap {
  position: relative;
}

#header {
  position: absolute;
  width: 50%;
}
#header::after {
  content: '';
  display: block;
  padding-bottom: 60%;
}
#pi0 {
  opacity: 0;
  transition: opacity 1s ease;
}
#pi1 {
  opacity: 0;
  transition: opacity 1s ease;
}
#pi2 {
  opacity: 0;
  transition: opacity 1s ease;
}
#pi-bg1 {
  width: 100%;
  height: 100%;
  background-image: url('../../assets/images/img1.jpg');
  background-position: center;
  filter: brightness(0.8);
  background-size: cover;
}
#pi-bg2 {
  width: 100%;
  height: 100%;
  background-image: url('../../assets/images/img2.jpg');
  background-position: center;
  filter: brightness(0.8);
  background-size: cover;
}
#pi-bg3 {
  width: 100%;
  height: 100%;
  background-image: url('../../assets/images/img3.jpg');
  background-position: center;
  filter: brightness(0.8);
  background-size: cover;
}
.pi-bg {
  position: absolute;
  width: 100px;
  height: 100px;
}
.pi-order {
  position: absolute;
  left: 5%;
  color: #fff;
  font-size: 30px;
}
.pi-count {
  position: absolute;
  left: 5%;
  margin-top: 30px;
  color: #fff;
  font-size: 30px;
}

#title {
  position: relative;
  width: 100%;
  z-index: 2;
}
#title::after {
  content: '';
  display: block;
  padding-bottom: 30%;
}
#pi-p-container {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: flex-end;
  position: absolute;
  width: 100%;
  height: 100%;
  background: var(#ffffff80); /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #ffffff80 10%, #fff 50%); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #ffffff80 10%, #fff 50%); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}
.pi-p {
  margin-right: 20px;
  margin-bottom: 0;
  cursor: pointer;
  transition: transform 0.8s ease;
}
.pi-p:hover {
  font-weight: 600;
  transform: scale(1.2);
}

.aheader {
  opacity: 1 !important;
  z-index: 1 !important;
}
.atitle {
  transform: scale(1.1);
  font-weight: 400;
}
</style>
