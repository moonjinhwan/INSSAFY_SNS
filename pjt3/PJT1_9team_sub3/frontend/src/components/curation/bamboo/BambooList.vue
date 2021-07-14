<template>
  <div>
    <!--https://www.npmjs.com/package/vue-masonry-css-->
    <masonry :cols="{ default: 3, 576: 1 }" :gutter="10" style="padding:12px 15px;">
      <div class="bamboo-description rounded-bg container">
        <h4 class="b-desc">대나무숲</h4>
        <p class="l-desc">
          마음 터놓고 이야기 해요.<br />
          내 마음을 편하게 이야기하는 공간입니다
        </p>
      </div>

      <BambooPost v-for="(post, idx) in posts" :key="idx" :post="post" />
    </masonry>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots">
      <div slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">
        목록의 끝입니다 :)
      </div>
    </infinite-loading>
  </div>
</template>

<script scoped>
import BambooPost from '@/components/curation/bamboo/BambooPost.vue';
import * as bambooApi from '@/api/bamboo';

import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'PostList',
  components: {
    BambooPost,
    InfiniteLoading,
    // Waterfall,
    // WaterfallSlot,
  },
  data() {
    return {
      posts: [],
      page: 0,
      handlerdata: '',
    };
  },
  computed: {
    writeFlag() {
      return this.$store.state.bamboo.flagBamboo;
    },
  },
  created() {},
  watch: {
    writeFlag: function() {
      // console.log('혹시 글쓰면 플래그 바뀌면서 이 함수 호출되나요?')
      this.page = 0;
      this.posts = [];
      this.infiniteHandler(this.handlerdata);
      // location.reload();
    },
  },
  methods: {
    infiniteHandler($state) {
      this.handlerdata = $state;
      const EACH_LEN = 15;
      bambooApi
        .getAllList({ page: this.page, size: EACH_LEN })
        .then((res) => {
          setTimeout(() => {
            if (res.data.bambooList) {
              this.posts = this.posts.concat(res.data.bambooList);
              this.page += 1;
              $state.loaded();
              if (res.data.bambooList.length / EACH_LEN < 1) {
                $state.complete();
              }
            } else {
              $state.complete();
            }
          }, 1000);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    fetchListData() {
      // page는 동적을 계속 바뀌어야 함
      bambooApi
        .getAllList({ page: 0, size: 20 })
        .then((res) => {
          this.posts = res.data.bambooList;
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.bamboo-description {
  margin-top: 1rem;
  color: #f9f9f9;
  background-color: #0b2945 !important;
}

.rounded-bg {
  border-radius: 15px;
  /* background-color: var(--basic-color-bg2); */
}

.container {
  width: 100%;
  padding: 10px;
  box-shadow: var(--basic-shadow-w);
}

@media screen and (max-width: 576px) {
  .bamboo-list {
    display: inline-block;
    min-width: 100%;
  }
  .bamboo-description {
    display: inline-block;
    min-width: 100%;
  }
}
</style>
