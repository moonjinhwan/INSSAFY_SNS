<template>
  <div>
    <v-dialog content-class="round" overlay-opacity="0" v-model="dialogState" max-width="400" transition="dialog-bottom-transition" @input="close">
      <v-card id="wrap">
        <v-btn class="deleteBtn" icon color="#AA2610" @click="deleteBtn">
          <v-icon dark>
            mdi-close-thick
          </v-icon>
        </v-btn>
        <v-card-title class="headline">{{ event.name }}</v-card-title>
        <v-card-text style="font-size: 1rem" class="font-weight-bold">{{ event.content }}</v-card-text>

        <div class="ml-5 font-weight-light">
          <div>시작일: {{ getEventStart() }}</div>
          <div>종료일: {{ getEventEnd() }}</div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="close">닫기</v-btn>
          <v-btn color="green darken-1" text @click="modify">수정</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
export default {
  name: 'EventDetail',
  data() {
    return {
      dialogState: false,
      start: '',
      startTime: '',
      end: '',
      endTime: '',
    };
  },
  computed: {
    ...mapState('calendar', ['detailDialog']),
    event() {
      return this.$store.getters['calendar/getEventToVue'];
    },
  },
  watch: {
    detailDialog(v) {
      this.dialogState = v;
    },
  },
  methods: {
    ...mapActions('calendar', ['actDeleteEvent']),
    getEventStart() {
      return this.event.start;
    },
    getEventEnd() {
      return this.event.end;
    },
    close() {
      return this.$store.commit('calendar/CLOSE_EVENT_DETAIL_DIALOG');
    },
    modify() {
      this.$store.commit('calendar/OPEN_MODIFY_CALENDAR_DIALOG', this.event);
    },
    deleteBtn() {
      this.$toast.open({
        position: 'top-right',
        duration: 1800,
        message: `클릭하여 '${this.event.name}' 일정 삭제`,
        type: 'error',
        onClick: () => {
          if (this.actDeleteEvent(this.event.id)) {
            this.$toast.open({
              message: '일정이 삭제되었습니다.',
              type: 'success',
            });
          }
        },
        queue: true,
      });
    },
  },
};
</script>

<style>
.round {
  border-radius: 15px;
}
</style>
<style scoped>
#wrap {
  border-radius: 15px !important;
}
.deleteBtn {
  position: absolute;
  right: 0;
}
</style>
