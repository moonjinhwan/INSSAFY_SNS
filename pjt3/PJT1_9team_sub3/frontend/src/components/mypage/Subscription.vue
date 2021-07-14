<template>
  <v-container
    class="my-0    
    mx-0
    px-1"
  >
    <div
      id="wrap"
      class="img-wrap
      d-flex"
    >
      <!-- 보드 이미지 위로 나오는 부분 -->
      <div class="text" @click.self="moveToBoard">
        <!-- 즐겨찾기 항목에 추가 -->
        <v-btn icon x-large fab :color="isFavorite" @click="addInFavoriteBoard">
          <v-icon dark>
            mdi-star
          </v-icon>
        </v-btn>
        <!-- board title -->
        <v-card-title
          class="flex-grow-1
          title"
          @click="moveToBoard"
        >
          {{ board_name }}
        </v-card-title>
        <!-- 삭제 버튼 -->
        <div class="align-self-start">
          <v-btn icon color="#AA2610" @click="removeBoard">
            <v-icon dark>
              mdi-close-thick
            </v-icon>
          </v-btn>
        </div>
      </div>
      <!-- 보드 이미지 부분 -->
      <GradientGenerator class="myinfo-list" style="height: 100px" v-if="board_image == null" :radius="radius" />
      <v-img height="100px" class="blur myinfo-list" v-if="board_image != null" :style="{ backgroundImage: `url(${board_image})` }"> </v-img>
    </div>
  </v-container>
</template>

<script>
import * as boardApi from '@/api/board';
import GradientGenerator from '@/components/etc/GradientGenerator';
import { mapActions } from 'vuex';
export default {
  name: 'Subscription',
  components: {
    GradientGenerator,
  },
  data() {
    return {
      isFavorite: '#ffc83d',
      board_name: '',
      board_image: null,
      radius: '15px',
    };
  },
  props: {
    board: Object,
  },
  created() {
    if (this.board.favorite_flag == 1) {
      this.isFavorite = '#ffc83d';
    } else {
      this.isFavorite = '#fff';
    }
  },
  mounted() {
    this.getBoardDetail();
    if (this.board.favorite_flag == 1) {
      this.isFavorite = '#ffc83d';
    } else {
      this.isFavorite = '#fff';
    }
  },
  methods: {
    ...mapActions('user', ['actPutFavorite']),
    openToast: function(msg, type) {
      this.$toast.open({
        message: msg,
        type: type,
      });
    },
    getBoardDetail() {
      boardApi
        .board_detail(this.board.board_id)
        .then((res) => {
          this.board_name = res.data.boardDto.board_name;
          if (res.data.boardDto.board_image != 'null' && res.data.boardDto.board_image != null && res.data.boardDto.board_image != '') {
            this.board_image = res.data.boardDto.board_image;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 즐겨찾는 보드에 추가
    addInFavoriteBoard() {
      if (this.board.favorite_flag == 1) {
        this.actPutFavorite({
          user_id: this.$store.state.auth.user.userId,
          board_id: this.board.board_id,
        });
        this.openToast(`즐겨찾기에서 ${this.board_name}를 제거했습니다.`, 'warning');
        this.isFavorite = '#fff';
        this.board.favorite_flag = 0;
      } else {
        //즐겨찾기 추가
        this.actPutFavorite({
          user_id: this.$store.state.auth.user.userId,
          board_id: this.board.board_id,
        });
        this.openToast(`즐겨찾기에서 ${this.board_name}를 추가했습니다.`, 'success');
        this.isFavorite = '#ffc83d';
        this.board.favorite_flag = 1;
      }
    },

    // 해당 보드로 이동(상세 주소 넘겨주기)
    moveToBoard() {
      this.$router.push({ name: 'Board', params: { board_id: this.board.board_id } });
    },
    // 보드 구독목록에서 삭제
    removeBoard: function() {
      this.$toast.open({
        position: 'top-right',
        duration: 1800,
        message: `클릭하여 '${this.board_name}' 보드 구독 취소`,
        type: 'error',
        onClick: () => {
          this.$emit('delSub', this.board.board_id);
        },
        queue: true,
      });
    },
  },
};
</script>

<style scoped>
/* board image 부분 */
.img-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  cursor: pointer;
}
/* 이미지 흐리게 하기 */
.blur {
  filter: brightness(50%);
  z-index: 1;
}
.text {
  position: absolute;
  width: 100%;
  height: 100%;
  color: #ffffff;
  display: flex;
  text-align: center;
  align-items: center;
  justify-content: space-around;
  z-index: 2;
}
.title {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
.myinfo-list {
  border-radius: 15px;
  background-color: #000;
  background-size: cover;
  background-position: center;
  box-shadow: var(--basic-shadow-s);
}
</style>
