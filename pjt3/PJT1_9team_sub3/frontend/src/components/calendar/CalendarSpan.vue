<template>
  <div id="wrap">
    <div id="header" data-app>
      <div id="title" class="b-title">
        <span>{{ date | moment('YYYY') }}</span>
        <div id="month">
          <b-icon class="chevron" icon="chevron-compact-left" @click="clickChevronLeft" />
          <span>{{ date | moment('MM') }}</span>
          <b-icon class="chevron" icon="chevron-compact-right" @click="clickChevronRight" />
        </div>
      </div>
      <v-menu ref="menu" v-model="menu" :close-on-content-click="false" transition="scale-transition" offset-y min-width="auto">
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="dateFormatted"
            label="Date"
            v-bind="attrs"
            @blur="date = parseDate(dateFormatted)"
            v-on="on"
            hide-details=""
            style="margin: 16px 10px 0 0; width: 20%; color: #000;"
          ></v-text-field>
        </template>
        <v-app style="height: 300px; overflow:hidden;">
          <v-date-picker id="picker" v-model="date" no-title @input="menu = false"></v-date-picker>
        </v-app>
      </v-menu>

      <v-select v-model="type" :items="typeOptions" label="Type" class="my-auto" hide-details style="margin: 0 4px 0 10px; width: 20%;"></v-select>
    </div>

    <v-app style="background-color:inherit; height:inherit">
      <div id="calendar-container" class="container-shadow" style="height: 550px">
        <v-calendar
          id="calendar"
          ref="calendar"
          :event-color="getEventColor"
          :events="events"
          :start="date"
          :type="type"
          @click:date="open"
          @click:event="showEvent"
          @click:more="moreEvent"
          @click:time="open"
          v-model="date"
        ></v-calendar>
      </div>
    </v-app>
  </div>
</template>

<script>
import moment from 'moment';
import { mapState } from 'vuex';

export default {
  name: 'CalendarSpan',
  props: {
    boardName: String,
  },
  data() {
    return {
      dateOpen: false,
      type: 'month',
      typeOptions: [
        { text: 'Day', value: 'day' },
        { text: 'Week', value: 'week' },
        { text: 'Month', value: 'month' },
      ],

      date: new Date().toISOString().substr(0, 10),
      dateFormatted: this.formatDate(new Date().toISOString().substr(0, 10)),
      menu: false,
    };
  },
  created() {
    this.$store.dispatch('calendar/actGetEvents', this.boardName);
  },
  computed: {
    computedDateFormatted() {
      return this.formatDate(this.date);
    },
    ...mapState('calendar', ['events']),
  },
  watch: {
    date(val) {
      this.dateFormatted = this.formatDate(this.date);
    },
  },
  methods: {
    //캘린더 내 활용 메서드
    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('-');
      return `${year}. ${month}. ${day}. `;
    },
    parseDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('. ');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },
    getEventColor(event) {
      return event.color;
    },

    //클릭 이벤트
    //캘린더 month 좌우 꺽쇠 아이콘
    clickChevronLeft() {
      const nextMoment = moment(this.date, 'YYYY-MM-DD').subtract(1, 'month');
      this.date = nextMoment.format('YYYY-MM-DD');
    },
    clickChevronRight() {
      const nextMoment = moment(this.date, 'YYYY-MM-DD').add(1, 'month');
      this.date = nextMoment.format('YYYY-MM-DD');
    },
    //버튼 이벤트
    open(date) {
      this.$store.commit('calendar/OPEN_CALENDAR_DIALOG', date);
    },
    showEvent({ event }) {
      this.$store.commit('calendar/OPEN_EVENT_DETAIL_DIALOG', event);
    },
    moreEvent({ date }) {
      this.type = 'day';
      this.date = date;
      this.$toast.open({
        message: '하루 일정으로 변경되었습니다.',
        type: 'default',
      });
    },
  },
};
</script>

<style scoped>
v-app {
  height: auto;
}
.v-btn::before {
  background-color: var(--basic-color-key);
}
#wrap {
  display: flex;
  flex-direction: column;
  width: 100%;
}
#header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 80px;
  padding: 5px 12px 0px;
  background-color: var(--basic-color-bg2);
}

#title {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 4px;
  margin-left: 2px;
  margin-right: 40px;
  font-size: 24px;
}
#month {
  margin-left: -5px;
}
.chevron {
  display: inline-block;
  width: 20px;
  height: 26px;
  cursor: pointer;
  transition: transform 0.5s ease;
}
.chevron:hover {
  transform: scale(1.2);
}
#calendar-container {
  margin: 10px;
  overflow: hidden;
  border-radius: 15px;
}
#calendar {
  width: calc(100% + 2px);
  height: calc(100% + 2px);
  margin-top: -1px;
  margin-left: -1px;
}

@media (max-width: 426px) {
  #title {
    margin-right: 10px;
  }
}
</style>
