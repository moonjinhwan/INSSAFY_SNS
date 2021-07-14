<template>
  <v-dialog content-class="round" overlay-opacity="0" max-width="600px" v-model="dialogState" transition="dialog-bottom-transition" @input="close">
    <v-card id="wrap">
      <v-card-title>
        <h3 id="title">일정 추가</h3>
      </v-card-title>
      <v-card-text>
        <v-form class="px-3" ref="form">
          <v-text-field label="일정" v-model="event.name" prepend-icon="mdi-folder-marker"></v-text-field>
          <v-textarea label="상세설명" v-model="event.content" prepend-icon="mdi-pencil"></v-textarea>
          <v-row>
            <v-col cols="6" class="pb-0">
              <v-menu>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    slot="activator"
                    label="시작일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="event.startDate"
                    class=""
                  ></v-text-field>
                </template>
                <v-app style="height:290px; overflow:hidden;">
                  <v-date-picker no-title v-model="event.startDate"></v-date-picker>
                </v-app>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-menu :close-on-content-click="false" v-model="startTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field label="시작 시간" readonly :value="event.startTime" prepend-icon="mdi-timer" v-on="on"></v-text-field>
                </template>
                <v-app style="height:460px; overflow:hidden;">
                  <v-time-picker v-if="startTimer" v-model="event.startTime">
                    <v-btn class="mx-auto" @click="selectTime">선택 </v-btn>
                  </v-time-picker>
                </v-app>
              </v-menu>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="6" class="pt-0">
              <v-menu>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    slot="activator"
                    label="종료일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="event.endDate"
                    class=""
                  ></v-text-field>
                </template>
                <v-app style="height:290px; overflow:hidden;">
                  <v-date-picker no-title v-model="event.endDate" :allowed-dates="allowedDates"></v-date-picker>
                </v-app>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pt-0">
              <v-menu :close-on-content-click="false" v-model="endTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field label="종료 시간" readonly :value="event.endTime" prepend-icon="mdi-timer" v-on="on"></v-text-field>
                </template>

                <v-app style="height:460px; overflow:hidden;">
                  <v-time-picker v-if="endTimer" v-model="event.endTime">
                    <v-btn class="mx-auto" @click="selectTime">선택 </v-btn>
                  </v-time-picker>
                </v-app>
              </v-menu>
            </v-col>
          </v-row>

          <div class="text-center" style="margin-bottom: 10px;">
            <v-btn text class="primary white--text mx-2 mt-3 bottom-btn newmorphism m-desc" @click="close">
              <span style="color:var(--basic-color-new);">취소</span>
            </v-btn>
            <v-btn text class="primary white--text mx-2 mt-3 bottom-btn newmorphism m-desc" @click="submit">
              <span style="color:var(--basic-color-key);">추가</span>
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from 'vuex';
export default {
  name: 'CalendarDialog',
  props: {
    boardName: String,
  },
  data() {
    return {
      dialogState: false,
      startTimer: false,
      endTimer: false,
      // titleMax: 16,
      // descMax: 40,
    };
  },
  computed: {
    ...mapState('calendar', ['dialog', 'event']),
    // titleRule() {
    //   return (v) => (v || '').length <= this.titleMax || `일정은 ${this.titleMax}자를 넘을 수 없습니다.`;
    // },
    // descRule() {
    //   return (v) => (v || '').length <= this.descMax || `상세내용은 ${this.descMax}자를 넘을 수 없습니다.`;
    // },
  },
  watch: {
    event: {
      deep: true,
      handler() {
        if (this.event.startTime != undefined && this.event.endTime != undefined && this.event.startTime != '' && this.event.endTime != '') {
          this.event.hasTime = true;
        }
      },
    },
    dialog(v) {
      this.dialogState = v;
    },
  },
  methods: {
    submit() {
      if (this.event.name === '' || this.event.endDate === '') {
        this.$toast.open({
          message: '일정을 작성한 뒤 다시 시도해주십시오.',
          type: 'warning',
        });
      } else {
        if (this.startTimer && this.endTime) this.event.hasTime = true;
        this.$store.dispatch('calendar/actAddEvent', { event: this.event, boardName: this.boardName }).then((result) => {
          if (result) {
            this.$toast.open({
              message: '일정이 등록되었습니다.',
              type: 'success',
            });
          }
        });
      }
    },
    close() {
      this.$store.commit('calendar/CLOSE_CALENDAR_DIALOG');
    },
    selectTime() {
      this.startTimer = false;
      this.endTimer = false;
    },
    allowedDates(val) {
      let endDate = val.split('-').reduce((a, b) => a + b);
      let startDate = this.event.startDate.split('-').reduce((a, b) => a + b);
      return endDate >= startDate;
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
  background-color: var(--basic-color-bg) !important;
  border-radius: 15px;
}
#title {
  margin-top: 10px;
}
.bottom-btn {
  border-radius: 30px;
  background: #ebebe9 !important;
  color: var(--basic-color-fill2) !important;
}
.bottom-btn:hover,
.bottom-btn:active {
  background: #ebebe9;
  background: linear-gradient(145deg, #d4d4d2, #fbfbf9);
  box-shadow: 10px 10px 20px #b3b3b1, -10px -10px 20px #ffffff;
}
</style>
