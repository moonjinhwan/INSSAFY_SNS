<template>
  <div class="frame">
    <p id="f-type" class="r-title">Suggest</p>
    <div class="bg-image" v-if="favorite.board_image" :style="{ backgroundImage: `url(${favorite.board_image})` }" />
    <GradientGenerator id="bg-graid" radius="0px" v-if="!favorite.board_image" />
    <div class="inner">
      <div id="f-title" class="f-text b-desc">
        <p>{{ favorite.board_name }}</p>
      </div>
      <p id="f-desc" class="f-text r-desc">{{ favorite.board_description }}</p>
      <p id="f-hashtag" class="f-text hashtag l-desc">
        {{ favorite.board_hash }}
      </p>
    </div>
    <!-- <div id="f-option" class="t-desc-e">
      <p>new</p>
      <p>{{ favorite.write_post_count }}</p>
    </div> -->
  </div>
</template>

<script>
export default {
  name: 'Slide',
  props: {
    favorite: Object,
  },
  components: {
    GradientGenerator: () => import('@/components/etc/GradientGenerator'),
  },
  data() {
    return {
      board_name: '츄쳔~~~~',
      board_description: '강추보드로 공간을 메우자!',
      board_hash: '#추천 #즐겨찾기해요',
      board_image: false,
    };
  },
};
</script>

<style scoped>
p {
  margin: 0;
}
.frame {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: var(--basic-color-fill);
}
.bg-image {
  position: absolute;
  z-index: 0;
  width: 90%;
  height: calc(100% - 80px);
  margin: 0 5%;
  color: #fff;
  background-image: url('../../assets/images/slide.jpg');
  background-position: center bottom;
  background-size: cover;
  /* box-shadow: var(--basic-shadow-s); */
  /* filter: brightness(0.9); */
}
#bg-graid {
  position: absolute;
  z-index: 0;
  width: 90%;
  height: calc(100% - 80px);
  margin: 0 5%;
}
.inner {
  position: relative;
  z-index: 1;
  width: 90%;
  height: calc(100% - 80px);
  margin: 0 auto;
  background: #00000033;
}

#f-type {
  color: var(--basic-color-fill3);
  text-align: center;
  padding: 11px 0 11px;
  width: 100%;
  font-size: 13px;
}
#f-title {
  font-size: 20px;
  text-align: center;
  /* position: absolute;
  z-index: 0 !important; */
  display: inline-block;
  width: auto;
  max-width: 95%;
  padding: 4px 30px;
  margin-top: 15%;
  margin-left: calc(-5% - 1px);
  box-shadow: var(--basic-shadow-b);
  background-color: var(--basic-color-key);
}
#f-title p {
  text-align: center;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
#f-desc {
  margin: 5% 20px 10%;
  font-size: 14px;
  line-height: 20px;
  max-height: 40px;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
#f-hashtag {
  font-size: 12px;
  line-height: 14px;
  margin: 15px 20px 0px;
  max-height: 28px;
  text-align: center;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.f-text {
  color: #fff;
}
.hashtag {
}
#f-option {
  font-size: 13px;
  color: var(--basic-color-bg);
  margin-top: 8px;
  margin-left: -15px;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  width: 100%;
}
#f-option p:first-child {
  transform: translate(10px, -8px);
  letter-spacing: -1px;
  font-weight: 900;
  color: #ff0000cc;
}
</style>
