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
            style="margin: 16px 10px 0 0; width: 20%;"
          ></v-text-field>
        </template>
        <v-date-picker v-model="date" no-title @input="menu = false"></v-date-picker>
      </v-menu>

      <v-select v-model="type" :items="typeOptions" label="Type" class="my-auto" hide-details style="margin: 0 4px 0 10px; width: 20%;"></v-select>
    </div>

    <div id="calendar-container" class="container-shadow">
      <v-calendar id="calendar" ref="calendar" :start="date" :type="type"></v-calendar>
    </div>
  </div>
</template>

<script>
import moment from 'moment';

export default {
  name: 'CalendarSpan',
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
  computed: {
    computedDateFormatted() {
      return this.formatDate(this.date);
    },
  },
  watch: {
    date(val) {
      this.dateFormatted = this.formatDate(this.date);
    },
  },
  methods: {
    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('-');
      return `${year}. ${month}. ${day}`;
    },
    parseDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('. ');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },
    clickChevronLeft: function() {
      const nextMoment = moment(this.date, 'YYYY-MM-DD').subtract(1, 'month');
      this.date = nextMoment.format('YYYY-MM-DD');
    },
    clickChevronRight: function() {
      const nextMoment = moment(this.date, 'YYYY-MM-DD').add(1, 'month');
      this.date = nextMoment.format('YYYY-MM-DD');
    },
  },
};
</script>

<style scoped>
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
  height: calc(100% - 100px);
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
