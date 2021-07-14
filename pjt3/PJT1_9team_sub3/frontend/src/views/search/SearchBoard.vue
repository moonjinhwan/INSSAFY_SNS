<template>
  <v-main class="main-bg-color">
    <v-container>
      <!-- 검색어 결과 노출 부분 -->
      <v-row>
        <v-col
          class="mb-4
          text-center
            text-body-1"
        >
          <h5 class="m-desc" v-if="payload.keyword !== ''">보드 중 "{{ payload.keyword }}"에 대한 검색 결과</h5>
          <h5 class="m-desc" v-if="payload.keyword === ''">보드 전체 검색 결과</h5>
        </v-col>
      </v-row>
      <!-- 검색 건수 / 정렬 / 보드 생성 -->
      <div
        class="d-flex 
          flex-row
          justify-content-end"
      >
        <!-- 보드 생성 버튼 -->
        <div>
          <v-btn id="create-btn" style="margin-bottom:20px" @click="goToCreateBoard()">
            <v-icon color="#AA2610">
              mdi-plus
            </v-icon>
            <div class="text-btn">보드 만들기</div>
          </v-btn>
        </div>
      </div>
      <!-- 보드 보여주는 부분 -->
      <v-row>
        <v-col class="col-12 col-sm-3" v-for="(board, idx) in searchList" :key="idx">
          <Board :board="board" class="real-shadow-text" />
        </v-col>
      </v-row>
      <infinite-loading @infinite="infiniteHandler" spinner="waveDots" ref="infiniteLoading">
        <div slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">
          목록의 끝입니다 :)
        </div>
      </infinite-loading>
    </v-container>
  </v-main>
</template>

<script>
import Board from '@/components/search/Board.vue';

import InfiniteLoading from 'vue-infinite-loading';
import { mapActions, mapMutations, mapState } from 'vuex';

export default {
  name: 'SearchBoard',
  components: {
    Board,
    InfiniteLoading,
  },
  data() {
    return {};
  },
  created() {
    this.payload.page = 0;
  },
  computed: {
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
    ...mapActions('search', ['actSearchAllBoard']),
    ...mapMutations('search', ['SET_NEXT_PAGE']),
    goToCreateBoard() {
      return this.$router.push({ name: 'BoardForm' });
    },

    infiniteHandler($state) {
      this.actSearchAllBoard().then((v) => {
        if (v) {
          setTimeout(() => {
            this.$store.commit('search/SET_NEXT_PAGE');
            $state.loaded();
          }, 600);
        } else {
          $state.complete();
        }
      });
    },
  },
};
</script>

<style scoped>
.main-bg-color {
  background-color: #ebebe9;
}
.sort-dropdown {
  max-width: 50%;
}
/* 검색결과들 담는 컨테이너 */
.search-result {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 0;
  padding-top: 0;
  flex-flow: wrap;
  border: 3px solid #000000;
}
/* 검색보드 감싸는 div */
.search-result-board {
  flex-basis: 19.5%;
  border: 3px dotted #000000;
}
/* 보드 만들기 버튼 */
#create-btn {
  position: sticky;
  text-align: center;
  margin: auto;
  height: 50px;
  width: 100%;
  border: none;
  color: var(--basic-color-fill);
  text-shadow: 0 0px 1px var(--basic-color-fill3);
  background: #ebebe9 !important;
  box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff;
  border-radius: 15px !important;
  transition: 0.3s all ease;
}
#create-btn:hover,
#create-btn:active,
#create-btn:focus {
  color: #ebebe9 !important;
  background-color: var(--basic-color-bg2) !important;
}
/* 보드 생성 버튼 */
.text-btn {
  color: #aa2610;
}
</style>
