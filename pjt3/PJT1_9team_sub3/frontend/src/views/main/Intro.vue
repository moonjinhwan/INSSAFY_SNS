<template>
  <div id="wrap">
    <!-- <video id="wrap" autoplay muted loop src="https://t1.kakaocdn.net/kakaocorp/main/kakao10/img/corp_pc.mp4?"></video> -->
    <video id="wrap" :poster="require(`@/assets/images/intro.jpg`)" playsinline autoplay muted loop>
      <!-- <source src="https://pjt1-image.s3.ap-northeast-2.amazonaws.com/%EB%82%98%EC%9D%98+%EB%8F%99%EC%98%81%EC%83%81+1_360p30.mov" type="video/webm"> -->
      <!-- <source :src="require(`@/assets/video/intro.mov`)" type="video/quicktime" /> -->
      <!-- <source src="http://thenewcode.com/assets/videos/fashion.mp4" type="video/mp4"> -->
    </video>
    <div class="cont">
      <p class="b-desc">소프트웨어로<br />더 나은 세상을 만드는 <br />싸피인입니다</p>
    </div>
    <!-- <div class="btn">
      <p>시작하기</p>
    </div> -->
    <!-- <img :src="require(`@/assets/images/intro.jpg`)" alt="" srcset=""> -->
  </div>
</template>
<script>
// import VideoBg from 'vue-videobg'
// import imageUrl from '@/assets/images/intro.jpg';
// import videoUrl from '@/assets/videos/intro.mov';
export default {
  name: 'Intro',
  data() {
    return {
      // videoUrl: videoUrl,
      // imageUrl: imageUrl,
    };
  },
  components: {
    // VideoBg,
  },
};
</script>
<style scoped>
.daemoon{
  position: relative;
  height: calc(100vh - 60px);
  box-sizing: border-box;
  /* color: #fff; */
  background-position: center;
}
#wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.25);
  /* z-index: 100; */
}
@media (min-width: 576px){

  .cont {
    position: absolute;
    left: 10.125rem;
    top: 10.375rem;
    right: auto;
    bottom: auto;
    max-width: 1920px;
    margin-left: auto;
    margin-right: auto;
    box-sizing: border-box;
    z-index: 2;
  }
  .cont p {
    color: #fff;
    font-size: 2.625rem;
    line-height: 3.25rem;
    letter-spacing: -0.09375rem;
    font-weight: 700;
  }
  .btn{
    position: absolute;
    left: 50.125rem;
    top: 40.375rem;
    right: auto;
    bottom: auto;
    max-width: 1920px;
    margin-left: auto;
    margin-right: auto;
    box-sizing: border-box;
    z-index: 2;
  }
  .btn p {
    color: #fff;
    font-size: 2.625rem;
    line-height: 3.25rem;
    letter-spacing: -0.09375rem;
    font-weight: 700;
  }
}
</style>
