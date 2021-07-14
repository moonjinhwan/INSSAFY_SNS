<template>
  <div id="container-box-item">
    <!-- D-day, 마감일시 -->
    <div id="date-info" :class="{ impend: dday > -1 }">
      <span id="i-span" class="b-desc-e">{{ label }}</span>
    </div>
    <!-- 채용일정 제목 -->
    <div id="post-info">
      <p id="i-title" class="m-desc">{{ event.name }}</p>
      <p id="i-date" class="l-desc">{{ event.start | moment('YY.MM.DD.') }}</p>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
export default {
  name: 'DueDateItem',
  props: {
    event: Object,
  },
  data() {
    return {
      dday: '',
      label: '',
    };
  },
  created() {
    //d-day 구하기
    let today = moment();
    let target = moment(this.event.start, 'YYYY-MM-DD HH:mm:ss');
    this.dday = Math.floor(moment.duration(today.diff(target)).asDays());
    this.dday === 0 ? (this.label = 'NOW') : this.dday > 0 ? (this.label = `D+${this.dday}`) : (this.label = `D${this.dday}`);
  },
};
</script>

<style scoped>
#container-box-item {
  padding: 8px;
  display: flex;
  flex-direction: row;
  align-items: stretch;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: var(--basic-shadow-c);
}

#post-info {
  display: flex;
  flex-direction: column;
  margin: 0 10px 0;
  flex-basis: 100%;
}
#i-title {
  padding: 0;
  width: 100%;
  margin: 0;
  font-size: 20px;
  overflow: hidden;
  display: -webkit-box;
  word-break: break-all;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
#i-date {
  padding: 0;
  margin: 0;
  font-size: 14px;
}
#date-info {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 0px;
  padding: 10px;
  font-size: 20px;
  color: #fff;
  border-radius: 10px;
  background-color: #949590;
  box-shadow: 0 0 1px rgb(148, 149, 144, 0.6), 0 0 2px rgba(148, 149, 144, 0.747), 0 0 4px rgba(148, 149, 144, 0.548),
    0 0 12px rgba(148, 149, 144, 0.541);
  transition: width 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.4);
}
.impend {
  background-color: rgb(255, 43, 43) !important;
  box-shadow: 0 0 1px rgba(255, 107, 107, 0.644), 0 0 2px rgba(255, 107, 107, 0.747), 0 0 4px rgba(255, 84, 84, 0.548),
    0 0 12px rgba(255, 42, 42, 0.541) !important;
}
#i-span {
  font-size: 18px;
  opacity: 0;
  display: inline-block;
  visibility: hidden;
  white-space: nowrap;
  transition: opacity 0.2s ease;
}

#container-box-item:hover #date-info {
  width: 100px;
}
#container-box-item:hover #i-span {
  visibility: visible;
  opacity: 1;
}
@media (hover: none) {
  #date-info {
    width: 100px;
  }
  #i-span {
    visibility: visible;
    opacity: 1;
  }
}
</style>
