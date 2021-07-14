<template>
  <v-card
    id="post-box"
    class="mx-2
      mt-4
      mb-2
      pb-0
      d-flex
      flex-column"
    color="F9F9F9"
    @click="goThisStudy"
  >
    <div
      class="img-wrap
      d-flex
      flex-column"
    >
      <!-- 보드 이미지 위로 나오는 부분 -->
      <div id="header-container" class="text">
        <div class="board-title">
          {{ group.board_name }}
        </div>
      </div>
      <GradientGenerator class="myinfo-list" style="height: 100px" v-if="group.board_image == null" :radius="radius" />
      <div
        class="myinfo-list blur"
        style="height: 100px"
        v-if="group.board_image != null"
        :style="{ backgroundImage: `url(${group.board_image})` }"
      />
    </div>

    <!-- 포스트 글 내용 -->
    <v-card-text
      id="post-contents"
      class="font-weight-bold
        py-0 pt-4 pl-auto"
    >
      {{ group.board_description }}
    </v-card-text>
    <v-card-actions>
      <v-list-item class="grow">
        <!-- <v-list-item-content>
          <v-list-item-title>
            <v-icon>
              mdi-account-group
            </v-icon>
            {{ `?` }}
          </v-list-item-title>
        </v-list-item-content> -->

        <!-- 포스트 좋아요/댓글 수 -->
        <v-row align="center" justify="end">
          <span style="float:right;">
            <v-icon>mdi-account-group</v-icon>
            {{ group.board_count }}
            <!-- <v-icon small> mdi-comment-processing </v-icon>
            {{ `?` }} -->
          </span>
        </v-row>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
import GradientGenerator from '@/components/etc/GradientGenerator';

export default {
  name: 'StudyGroup',
  data() {
    return {
      radius: '15px',
    };
  },
  props: {
    group: Object,
  },
  components: {
    GradientGenerator,
  },
  computed: {
    image() {
      return this.group.board_image;
    },
  },
  methods: {
    goThisStudy() {
      this.$router.push({
        name: 'Study',
        params: {
          board_id: this.group.board_id,
        },
      });
    },
  },
};
</script>

<style scoped>
/* 해당 스터디로 이동시키는 부분 pointer */
#post-box {
  cursor: pointer;
  border-radius: 15px;
  box-shadow: var(--basic-shadow-c);
}
/* 이미지 흐리게 하기 */
.blur {
  filter: brightness(50%);
  z-index: 1;
}
.text {
  position: absolute;
  width: calc(100% - 20px);
  margin: 0 !important;
  height: 100px;
  color: #ffffff;
  display: flex;
  text-align: center;
  justify-content: center;
  align-items: center;
  z-index: 2;
}
/* 보드로 이동 */
.board-title {
  flex-grow: 100%;
  cursor: pointer;
}
/* 게시글 제목 넘치는 부분 처리 */
#post-title {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
/* 날짜 넘치는 부분 처리 */
#post-date {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
@media (max-width: 426px) {
  #post-date {
    font-size: 10px !important;
  }
}
/* 댓글 내용 넘치는 부분 처리 */
#post-contents {
  font-size: 14px;
  line-height: 20px;
  max-height: 40px;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.myinfo-list {
  border-radius: 15px;
  box-shadow: var(--basic-shadow-m);
}
</style>
