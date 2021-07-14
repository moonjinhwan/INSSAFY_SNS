<template>
  <div>
    <div class="join">
      <h2>회원가입</h2>
      <div class="join-form">
        <div class="join-input">
          <div class="input-with-label">
            <div class="indicator" :class="{ pass: indicator.email }" />
            <input
              v-model="email"
              id="email"
              placeholder="이메일을 입력하세요."
              type="text"
              :class="{ error: error.email && email.length !== 0, complete: !error.email && email.length !== 0 }"
              @blur="onDuplicate"
              @focus="waitDuplicate"
              autocapitalize="off"
            />
            <Delete-btn class="delete-btn" :state="email" @clickDel="deleteEmail" />
            <label for="email">이메일*</label>
            <div class="error-text" v-if="error.email && email.length !== 0">{{ error.email }}</div>
            <div class="error-text" v-if="error.emailDuplicate">{{ error.emailDuplicate }}</div>
          </div>
          <div class="input-with-label">
            <label for="password">비밀번호</label>
            <input
              v-model="password"
              id="password"
              :type="passwordType"
              placeholder="비밀번호를 입력하세요."
              :class="{
                error: error.password && password.length !== 0,
                complete: !error.password && password.length !== 0,
              }"
            />
            <Delete-btn class="delete-btn" :state="password" @clickDel="deletePassword" />
            <label for="password">비밀번호*</label>
            <div class="error-text" v-if="error.password && password.length !== 0">{{ error.password }}</div>
          </div>
          <div class="input-with-label">
            <input
              v-model="passwordConfirm"
              :type="passwordConfirmType"
              id="password-confirm"
              placeholder="비밀번호를 재입력하세요."
              :class="{
                error: error.passwordConfirm && passwordConfirm.length !== 0,
                complete: !error.password && passwordConfirm.length !== 0,
              }"
            />
            <Delete-btn class="delete-btn" :state="passwordConfirm" @clickDel="deletePasswordConfirm" />
            <label for="password-confirm">비밀번호 확인*</label>
            <div class="error-text" v-if="error.passwordConfirm && passwordConfirm.length !== 0">
              {{ error.passwordConfirm }}
            </div>
          </div>
          <div id="problem-container" class="input-with-label">
            <p class="problem b-desc">다음은 학사규정 중 일부이다.</p>
            <b-icon
              id="new-problem"
              icon="arrow-clockwise"
              style="width:24px; height:24px;"
              @click="clickNewProblem"
              :class="{ rotate: state.rotate }"
            />
            <p class="problem r-desc">{{ problem }}</p>
          </div>
          <div class="input-with-label">
            <div class="indicator" :class="{ pass: indicator.solution }" />
            <label for="solution">인증문제 답*</label>
            <input
              id="solution"
              :class="{
                error: error.solution && solution.length !== 0,
                complete: !error.solution && solution.length !== 0,
              }"
              placeholder="빈 칸에 들어갈 알맞은 단어는?"
              type="text"
              v-model="solution"
              @blur="confirmSolution"
              @focus="waitSolution"
            />
            <Delete-btn class="delete-btn" :state="solution" @clickDel="deleteSolution" />
            <div class="error-text" v-if="error.solution && solution.length !== 0">{{ error.solution }}</div>
          </div>
          <div class="input-with-label">
            <label for="nickname">닉네임*</label>
            <input id="nickname" placeholder="닉네임을 입력하세요." type="text" v-model="nickname" />
            <Delete-btn class="delete-btn" :state="nickname" @clickDel="deleteNickname" />
          </div>
          <div class="input-with-label">
            <label for="location">지역*</label>
            <select ref="select0" class="join-style" name="location" id="location" v-model="location">
              <option value="" disabled selected>지역을 선택하세요.</option>
              <option v-for="location in options.location" :key="location" :value="location">
                {{ location }}
              </option>
            </select>
            <b-icon class="down-arrow" icon="arrow-down-short" />
          </div>
          <div class="input-with-label">
            <label for="location">기수*</label>
            <select ref="select1" class="join-style" name="location" id="location" v-model="generation">
              <option value="" disabled selected>기수를 선택하세요.</option>
              <option v-for="generation in options.generation" :key="generation" :value="generation">
                {{ generation }}
              </option>
            </select>
            <b-icon class="down-arrow" icon="arrow-down-short" />
          </div>
        </div>
        <!-- <button class="btn-join">로그인</button> -->
        <button class="btn-join b-title" @click="onJoin" :disabled="!isSubmit" :class="{ disabled: !isSubmit }">
          Join
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import PV from 'password-validator';
import * as EmailValidator from 'email-validator';
import * as authApi from '@/api/auth';
import * as join from '@/api/join';

import DeleteBtn from '@/components/etc/DeleteBtn.vue';
export default {
  name: 'Join',
  components: {
    DeleteBtn,
  },
  data() {
    return {
      options: {
        location: ['서울', '대전', '구미', '광주'],
        generation: [5, 4, 3, 2, 1],
      },
      email: '',
      password: '',
      passwordSchema: new PV(),
      passwordConfirm: '',
      problem: '',
      solution: '',
      nickname: '',
      location: '',
      generation: '',
      isLoading: false,
      error: {
        email: false,
        emailDuplicate: false,
        password: false,
        passwordConfirm: false,
        solution: false,
        nickname: true,
        location: true,
        generation: true,
      },
      indicator: {
        email: false,
        solution: false,
      },
      isSubmit: false,
      passwordType: 'password',
      passwordConfirmType: 'password',
      termPopup: false,
      state: {
        rotate: false,
      },
    };
  },
  created() {
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();
  },
  mounted() {
    //화면 적재되면 문제 랜덤으로 가져오기
    this.problem = this.$store.getters.getRandomProblem;
  },
  watch: {
    email: function() {
      this.checkForm();
    },
    password: function() {
      this.checkForm();
    },
    passwordConfirm: function() {
      this.checkForm();
    },
    nickname: function() {
      this.checkForm();
    },
    location: function() {
      this.checkForm();
    },
    generation: function() {
      this.checkForm();
    },
    solution: function() {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {
      this.email = this.email.toLowerCase();
      if (this.email.length >= 0 && !EmailValidator.validate(this.email)) this.error.email = '이메일 형식이 아닙니다.';
      else this.error.email = false;

      if (this.password.length >= 0 && !this.passwordSchema.validate(this.password))
        this.error.password = '영문,숫자 포함 8 자리이상이어야 합니다.';
      else this.error.password = false;

      if (this.passwordConfirm.length >= 0 && this.password != this.passwordConfirm)
        this.error.passwordConfirm = '비밀번호가 서로 같지 않습니다.';
      else this.error.passwordConfirm = false;

      if (this.nickname.length == 0) this.error.nickname = true;
      else this.error.nickname = false;

      if (this.location.length == 0) this.error.location = true;
      else this.error.location = false;

      if (this.generation.length == 0) this.error.generation = true;
      else this.error.generation = false;

      let isSubmit = true;
      Object.values(this.error).map((v) => {
        if (v) isSubmit = false;
      });
      Object.values(this.indicator).map((v) => {
        if (!v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    onJoin: function() {
      //spinner 동작
      this.$store.commit('setSpinnerTogle');
      this.$store.commit('setSpinnerMessage', '메일 발송 중');
      var member = {
        // 사용중으로 추가
        is_used: 1,
        user_email: this.email,
        user_password: this.password,
        user_nickname: this.nickname,
        user_location: this.location,
        user_generation: this.generation,
      };
      authApi
        .join(member)
        .then((response) => {
          //spinner 해제
          this.$store.commit('setSpinnerTogle');
          console.log(response.data);
          //상단 이메일 인증 알림
          this.$store.commit('auth/setEmail', this.email);
          this.$store.commit('setToastTogle');
          this.$store.commit('setToastType', 'email');
          alert(this.nickname + '님 환영합니다!');
          // this.$router.push({name: 'JoinSuccess', params:{email:this.email}});
          this.$router.push({ name: 'Main' });
        })
        .catch((error) => {
          //spinner 해제
          this.$store.commit('setSpinnerTogle');
          console.log(error);
          alert('가입에 실패하였습니다.');
          this.password = '';
          this.passwordConfirm = '';
        });
    },
    onDuplicate: function() {
      console.log(this.email);
      authApi
        .duplicate(this.email)
        .then((response) => {
          console.log(response);
          if (response.data.message === 'SUCCESS') {
            this.error.emailDuplicate = false;
            this.indicator.email = true;
          } else {
            this.indicator.email = false;
            this.checkForm();
            this.error.email = '이미 가입되어 있는 이메일입니다.';
          }
        })
        .catch((error) => {
          this.indicator.email = false;
          this.checkForm();
          this.error.email = '이미 가입되어 있는 이메일입니다.';
          console.log(error);
        });
    },
    waitDuplicate: function() {
      this.emailDuplicate = false;
    },

    //인증문제 처리
    clickNewProblem: function() {
      this.state.rotate = true;
      setTimeout(() => {
        this.state.rotate = false;
      }, 600);
      this.problem = this.$store.getters.getRandomProblem2(this.getRandomProblem);
    },
    confirmSolution: function() {
      join
        .confirmSolution(this.solution)
        .then((response) => {
          console.log(response);
          if (response.data.message == 'SUCCESS') {
            this.error.solution = false;
            this.indicator.solution = true;
          } else {
            this.error.solution = '인증 문제 답을 잘못 입력하셨습니다.';
            this.indicator.solution = false;
          }
          this.checkForm();
        })
        .catch((e) => {
          console.log(e);
        });
    },
    waitSolution: function() {
      // this.solution = '';
      this.error.solution = false;
    },

    //입력 문자 모두 지우는 버튼
    deleteEmail: function() {
      this.email = '';
    },
    deletePassword: function() {
      this.password = '';
    },
    deletePasswordConfirm: function() {
      this.passwordConfirm = '';
    },
    deleteNickname: function() {
      this.nickname = '';
    },
    deleteSolution: function() {
      this.solution = '';
    },
  },
};
</script>

<style scoped>
.join {
  display: flex;
  flex-direction: column;
  justify-content: center;
  /* align-items: center; */
  max-width: 580px;
  width: 100%;
  margin: 50px auto;
  background-color: var(--basic-color-bg2);
  box-shadow: var(--basic-shadow-s);
  border-radius: 10px;
  padding: 30px 30px;
}
@media (max-width: 426px) {
  .join {
    margin: 10px auto;
  }
}

h2 {
  margin-bottom: 30px;
}

.indicator {
  position: absolute;
  margin-top: 23px;
  width: 6px;
  height: 6px;
  background-color: red;
  border-radius: 6px;
  transition: background-color 0.5s ease;
}
.pass {
  background-color: rgb(0, 223, 67);
}

.delete-btn {
  position: absolute;
  right: 30px;
}

.down-arrow {
  position: absolute;
  right: 15px;
  margin-top: 15px;
}
.join-form {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  /* align-items: center; */
  /* height: 40vh; */
  width: 100%;
}
.join-input {
  width: 100%;
  float: left;
  position: relative;
  margin-bottom: 10px;
}
#problem-container {
  margin: 10px 10px 0;
}
#new-problem {
  position: absolute;
  right: 25px;
  margin-top: -20px;
  cursor: pointer;
}

.problem {
  margin: 0 0;
}

input,
select {
  background: transparent;
  font-size: 1em;
  width: 100%;
  height: 50px;
  line-height: 1em;
  padding: 0 20px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  -webkit-transition: 0.2s;
  transition: 0.2s;
  border-radius: 0;
  outline: none;
}
.error-text {
  font-size: 0.8rem;
  color: #ff0404;
}

.btn-join {
  /* position: inherit;  */
  height: 50px;
  margin-top: 10px;
  font-size: 24px;
  border: solid 1px #000;
  text-align: center;
}
.btn-join:hover,
.btn-join:active {
  background-color: #000 !important;
  color: #fff;
}

@media screen and (max-width: 576px) {
  .btn-join {
    width: 100%;
    background-color: #000 !important;
    color: #fff;
  }
}

/* 스켈레톤에서 가져온거 */
.disabled {
  color: var(--basic-color-bg);
  border: solid 1px var(--basic-color-bg) !important;
}

.input-with-label {
  width: 100%;
  float: left;
  position: relative;
  margin-bottom: 10px;
}

.input-with-label input[type='text'],
.input-with-label select,
.input-with-label input[type='password'] {
  width: 100%;
  float: left;
  height: 50px;
  line-height: 1;
  padding: 2px 35px 0 120px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #000;
  border-bottom: 1px solid var(--basic-color-bg);
}

.input-with-label input:hover,
.input-with-label input:focus,
.input-with-label select:hover,
.input-with-label select:focus,
.input-with-label input:hover,
.input-with-label input:focus {
  border-bottom: 1px solid #000;
}

.input-with-label input[type='text'].error,
.input-with-label input[type='password'].error {
  border-radius: 0;
  border: none;
  border-bottom: 1px solid #ee4b55;
}

.input-with-label input[type='text'].complete,
.input-with-label input[type='password'].complete {
  border-radius: 0;
  border: none;
  border-bottom: 1px solid #3893a8;
}

.input-with-label input[type='text'].disabled,
.input-with-label input[type='password'].disabled {
  background: #eee;
  color: #333;
  font-weight: 600;
}

.input-with-label input[type='text'].disabled:hover,
.input-with-label input[type='text'].disabled:focus,
.input-with-label input[type='password'].disabled:hover,
.input-with-label input[type='password'].disabled:focus {
  border: 1px solid #000;
}

.input-with-label label {
  position: absolute;
  left: 15px;
  top: 16px;
  color: #000;
  font-weight: 600;
  font-size: 0.857em;
}

.input-with-label h4 {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  word-wrap: normal;
}

.input-with-label .error-text {
  width: 100%;
  float: left;
  color: #ee4b55;
  margin-top: 3px;
}

.label-with-input {
  width: 100%;
  float: left;
  margin-bottom: 15px;
}

.label-with-input label {
  font-size: 1em;
  font-weight: 600;
  margin-bottom: 10px;
  display: block;
}

.label-with-input input[type='text'] {
  width: 100%;
  float: left;
}

.label-with-input input[type='text'].error {
  border-color: #ee4b55;
}

.label-with-input:last-of-type {
  margin-bottom: 0;
}

.label-with-input .error-text {
  width: 100%;
  float: left;
  color: #ee4b55;
  margin-top: 3px;
}
</style>
