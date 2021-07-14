<template>
  <div>
    <div>
      <Post v-for="(post, idx) in searchList" :key="idx" :post="post" :flagComponent="flagComponent" />
    </div>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots" ref="infiniteLoading">
      <div slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">
        목록의 끝입니다 :)
      </div>
    </infinite-loading>
  </div>
</template>

<script scoped>
import Post from '@/components/board/Post.vue';
// import {mapState} from 'vuex'P

import InfiniteLoading from 'vue-infinite-loading';

import { mapActions, mapMutations, mapState } from 'vuex';

export default {
  name: 'PostList',
  data() {
    return {
      posts: [],
      page: 0,
    };
  },
  components: {
    Post,
    InfiniteLoading,
  },
  created() {
    const curationName = this.$route.name;
    let BOARD_ID;
    if (curationName === 'Board' || curationName === 'Study') {
      BOARD_ID = Number(this.$route.params.board_id);
    } else {
      BOARD_ID = this.$store.state.curationId[curationName];
    }

    this.SET_PAYLOAD_RESET();
    this.SET_PAYLOAD_ID({
      type: 'title',
      board_id: BOARD_ID,
      user_id: this.$store.state.auth.user.userId,
    });
  },
  mounted() {
    this.payload.page = 0;
  },
  computed: {
    writeFlag() {
      return this.$store.state.board.writeFlag;
    },
    // 재사용의 핵심
    flagComponent() {
      let flag = {
        state: false,
        headerMarket: false,
        headerLearnShare: false,
      };
      const curationName = this.$route.name;
      if (curationName === 'Market') {
        flag.state = true;
        flag.headerMarket = true;
        return flag;
      } else if (curationName === 'LearnShare') {
        flag.state = false;
        flag.headerLearnShare = true;
      }
      return flag;
    },
    re() {
      return this.$route.params.board_id;
    },

    ...mapState('search', ['searchList', 'size', 'payload']),
  },
  watch: {
    payload: {
      deep: true,
      handler(v) {
        if (v.page == 0) this.$refs.infiniteLoading.stateChanger.reset();
      },
    },
  },
  methods: {
    ...mapActions('search', ['actSearchPost']),
    ...mapMutations('search', ['SET_NEXT_PAGE', 'SET_PAYLOAD', 'SET_PAYLOAD_ID', 'SET_PAYLOAD_RESET']),

    infiniteHandler($state) {
      // if(curationName!="Board" && curationName!="Study"){
      // }else{
      // }
      this.actSearchPost().then((v) => {
        if (v) {
          setTimeout(() => {
            this.$store.commit('search/SET_NEXT_PAGE');
            $state.loaded();
          }, 1600);
        } else {
          $state.complete();
        }
      });
    },
  },
};
</script>

<style></style>
