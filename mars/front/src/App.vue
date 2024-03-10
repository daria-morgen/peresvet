<template>
  <div class="reports_list">
    <div class="chat-container">
      <div class="chat-header head_font_style">
        <h2>ЗЕНИТ МАРС</h2>
      </div>
      <div class="outgoing-rect_send_button">
          <div class="outgoing-messages-rect">
            <div>исходящие</div>
          </div>
          <div class="send_button_new">
            <button @click="showModal = true" class="send_button_new">+ новый отчет</button>
          </div>
      </div>
      <div class="chat-messages" id="chat-messages">
        
        <div v-for="(report, index) in reports" :key="index" class="message-container">
          <div class="message-content white_color">
            <div>
              <span class="author">{{ report.authorName }}</span>
            </div>
            <div class="chat-message " :class="{ 'expanded': report.expanded }">
              {{ report.expanded ? report.reportText : truncatedText(report.reportText) }}
            </div>
          </div>
          <div class="report_size white_color">
            <span>
              {{ report.reportSize }} kB
            </span>
          </div>
          <div class="status_date_container">
            <div :class="getStatusClass(report.status) + ' report_status'">
              <span>
                {{ getReportStatus(report.status) }}
              </span>
            </div>
            <span class="report_timestamp">{{ format_date(report.dateCreation) }}</span>
          </div>

          <div class="expand_button_container">
            <button :disabled="!report.expanded && (report.reportText.length <= 70)" @click="toggleMessage(report)"
              :class="!report.expanded ? transformButton() + ' expand-button' : 'expand-button'">^
            </button>
          </div>
        </div>
      </div>
      <!-- use the modal component, pass in the prop -->
      <Transition name="modal_input" v-if="showModal" @close="showModal = false">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-container">
              <div class="modal-body">
                <div class="sender_container">
                  <div><label class="white_color">Отправитель: </label></div>
                  <input v-model="currentAuthorName" type="text" class="message-input"
                    :placeholder="inputAuthorNamePlaceholder">
                </div>
                <div class="report_container">
                  <div><label class="white_color">Отчет на землю: </label></div>
                  <input v-model="newReport.reportText" type="text" class="message-input message-input-report"
                    :placeholder="inputPlaceholder">
                </div>
              </div>

              <div class="modal-footer">
                <div>
                  <button class="modal_default_button" @click="showModal = false">
                    отмена
                  </button>
                </div>
                <div>
                  <button @click="validateAndSendMessage()" class="modal_send_button">отправить</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Transition>

    </div>
  </div>
</template>

<script>
import moment from 'moment'

export default {
  name: 'App',
  components: moment,
  data() {
    return {
      reports: [],
      newMessage: '', // Добавим новое свойство для хранения вводимого сообщения
      inputAuthorName: 'От...',
      inputAuthorNamePlaceholder: 'Введите имя...',
      inputPlaceholder: '',
      currentAuthorName: '',
      urlString: 'http://193.124.114.82:8081/api/reports',
      newReport: {
        authorName: '',
        reportText: '',
        dateCreation: '',
        expanded: false
      },
      showModal: false
    };
  },
  methods: {
    format_date(value) {
      if (value) {
        return moment(String(value)).format('yyyy-MM-DD HH:mm:ss')
      }
    },
    getReportList() {
      this.axios.get(this.urlString).then((response) => (this.reports = response.data));
    },
    toggleMessage(report) {
      // Прежде чем изменить значение, проверьте, есть ли текст в отчете
      if (report.reportText && report.reportText.length > 0) {
        report.expanded = !report.expanded;
      }
    },
    validateAndSendMessage() {
      if (!this.currentAuthorName.trim()) {
        const enteredName = window.prompt('Введите ваше имя:', '');
        if (enteredName && enteredName.trim()) {

          this.currentAuthorName = enteredName.trim();
          this.sendMessage();
        } else {
          alert('Пожалуйста, введите ваше имя.');
        }
      } else {
        this.sendMessage();
      }
    },
    sendMessage() {
      this.newReport.authorName = this.currentAuthorName;
      this.newReport.dateCreation = this.format_date(new Date());
      this.newReport.reportSize = this.getRandomReportSize();
      this.newReport.status = 'CREATED'

      this.axios.post(this.urlString, this.reportToJson(this.newReport),
                {
                  headers: {
                    "content-type": "application/json",
                  },
                }
            );

      this.inputPlaceholder = this.currentAuthorName + ', ведите сообщение...'

      this.reports.push(this.newReport);

      this.newReport = {
        authorName: '',
        reportText: '',
        dateCreation: '',
        reportSize: ''
      };
      this.showModal = false;

    },
    reportToJson(reportToJson) {
      return "{\"authorName\":\"" + reportToJson.authorName + "\",\n" +
        "    \"reportText\":\"" + reportToJson.reportText + "\",\n" +
        "    \"dateCreation\":\"" + reportToJson.dateCreation + "\",\n" +
        "    \"reportSize\":\"" + reportToJson.reportSize + "\"\n" +
        "}";
    },
    truncatedText(text) {
      const maxLength = 70;
      if (text.length > maxLength) {
        return text.substring(0, maxLength) + '...';
      } else {
        return text;
      }
    }
    , scrollToBottom() {
      const chatMessages = document.getElementById('chat-messages');
      chatMessages.scrollTop = chatMessages.scrollHeight;
    },
    getRandomReportSize() {
      return Number((Math.random() * (1000 - 1 + 1) + 1).toFixed(2))
    },
    getStatusClass(status) {
      // Возвращаем класс в зависимости от значения статуса
      switch (status) {
        case 'SUCCESS':
          return 'success-status';
        case 'SENDING':
          return 'sending-status';
        case 'ERROR':
          return 'error-status';
        // Добавьте другие кейсы, если необходимо
        default:
          return 'default-status';
      }
    },
    transformButton() {
      // Возвращаем класс 
      return 'transform_button';
    },
    getReportStatus(status) {
      switch (status) {
        case 'SUCCESS':
          return 'Доставлено';
        case 'SENDING':
          return 'Отправка...';
        case 'ERROR':
          return 'Не отправлено';
        default:
          return 'Создано';
      }
    }
  },
  beforeMount() {
    this.getReportList();

    setTimeout(() => {
      this.scrollToBottom();
    }, 500);

  }
};
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: black;
}

.chat-container {
  max-width: 70%;
  margin: 50px auto;

  overflow: hidden;
  box-shadow: 15px 15px 20px rgba(0, 0, 0, 0.5);
  padding: 10px;
}

.chat-header {
  padding: 10px;
  text-align: center;

}

.chat-input {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

.chat-input input {
  flex: 1;
  padding: 8px;
}

.chat-input button {
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  border-radius: 3px;
  margin-right: 5px;
}

.chat-messages {
  max-height: 500px;
  overflow-y: auto;
  overflow-x: hidden;
}

.message-container {
  display: flex;
  align-items: center;
  flex-direction: row;
  /* Изменяем направление флекс-контейнера на строку */
  text-align: left;
  /* Выравниваем текст слева */
  position: relative;
  margin-bottom: 10px;
  /* padding-top: 5px; */
  margin-right: 10px;
  background: #222222;
  border-radius: 15px;
  border: 1px;
  box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.5);
  padding: 5px;


}

.user-details .author {
  font-weight: bold;
}

.report_timestamp {
  color: #ffffff;
  font-family: Montserrat;
  font-size: 14px;
  font-weight: 400;
  line-height: 32px;
  letter-spacing: 0.2em;
  text-align: right;

}

.message-content {
  display: flex;
  flex-direction: column;
  padding: 10px;
  /* Изменяем направление флекс-контейнера на колонку */
  /*padding-right: 10px;!* Изменяем направление флекс-контейнера на колонку *!*/
  width: 100%;
}

.chat-message,
.chat-message.expanded {
  display: flex;
  flex-wrap: wrap;
  /* Позволяет элементам переноситься на следующую строку */
  justify-content: flex-start;
  /* Размещаем содержимое слева */
  width: 100%;
}

.chat-message {
  border-radius: 5px;
  overflow: hidden;
  margin-bottom: 10px;
  word-wrap: break-word;
  /* Добавляем перенос слов */
  word-break: break-all;

  src: url('fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf') format('truetype');
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 400;
  line-height: 30px;
  letter-spacing: 0em;
}

.chat-message.expanded {
  white-space: normal;
  text-overflow: initial;
}

.expand-button {
  background-color: var(--container-bg-color);
  color: #ffffff;
  background-color: #323232;
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  border-radius: 50%;
  font-size: 20px;
  /*margin-top: 5px; !* Добавляем отступ сверху *!*/
  box-shadow: none;
  border: none;
  /*margin-left: auto;*/
}

.transform_button {
  transform: rotate(180deg);
}

.expand-button:disabled,
.send-button:disabled {
  color: #565656;
}

.status_date_container {

  display: flex;
  flex-direction: column;

  margin: 10px;
}

.report_status {
  border: 1px solid;
  display: flex;
  flex-direction: column;
  border-radius: 27px;
  width: 176px;
  height: 32px;
  align-items: center;
}

.report_status span {
  margin: auto;
}

.chat-messages::-webkit-scrollbar {
  width: 10px;
  /* Увеличиваем ширину скроллбара */
}

.chat-messages::-webkit-scrollbar-thumb {
  background-color: #ccc;
  outline: 1px solid #ccc;
  margin-left: 10px;
  /* Добавляем отступ слева */
}

.chat-messages::-webkit-scrollbar-track {
  background: transparent;
}

/* Скрытие горизонтального скролла */
.chat-messages::-webkit-scrollbar-horizontal {
  display: none;
}

.success-status {
  color: green;
}

.error-status {
  color: red;
}

.default-status {
  color: #7e8cd4;
}

.sending-status {
  color: #d5cd17;
}

.head_font_style {
  color: #49FFE9;
  font-family: Montserrat;
  font-size: 32px;
  font-weight: 700;
  line-height: 39px;
  letter-spacing: 1.11em;
  text-align: left;

  src: url('fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf') format('truetype');
}

.outgoing-messages-rect {
  max-width: 388px;
  height: 72px;
  border-radius: 15px;
  border: 1px;
  border: 1px solid #5A5A5A;
  text-align: center;
  /* align-items: center; */
  display: flex;
  align-items: center;
  flex-direction: row;
}

.outgoing-messages-rect div {
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 400;
  line-height: 32px;
  letter-spacing: 0.81em;
  text-align: center;
  color: #E1E1E1;
  margin: auto;
  padding: 10px;
  src: url('fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf') format('truetype');
}

.outgoing-rect_send_button {
  padding: 10px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  /* flex-direction: row; */
  justify-content: space-between;
}

.send_button_new {
  max-width: 388px;
  height: 72px;
  border-radius: 15px;
  border: 1px;
  border: 1px solid #5A5A5A;
  color: #49FFE9;
  background: #222222;
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 400;
  line-height: 32px;
  letter-spacing: 0.81em;
  text-align: center;
  src: url('fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf') format('truetype');
  white-space: nowrap;
  /* margin-left: auto; */

}

.white_color {
  color: #FFFFFF;

}

.expand-button-container {
  padding: 10px;
  /* angle: -180 deg; */
}

.report_size {
  font-size: 10px;
  font-family: Montserrat;
  src: url('fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf') format('truetype');
  font-size: 14px;
  font-weight: 400;
  line-height: 32px;
  letter-spacing: 0.2em;
  text-align: right;
  padding: 10px;

}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  /* display: table; */
  transition: opacity 0.3s ease;
}

.modal-container {
  width: 50%;
  height: 40%;
  margin: 0px auto;
  padding: 20px 30px;
  background-color: #222222;
  transition: all 0.3s ease;
  border: 1px solid #FFFFFF;
  top: 289px;
  left: 478px;
  border-radius: 15px;
  border: 1px;

}

.modal-header h3 {
  margin-top: 0;
  color: #42b983;
}

.modal-body {
  margin: 20px 0;
  font-family: Montserrat;

  display: flex;
  flex-direction: column;
  font-size: 12px;
  font-weight: 400;
  line-height: 32px;
  letter-spacing: 0em;
  text-align: left;

}

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}

.message-input {
  background: #171717;
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 400;
  line-height: 30px;
  letter-spacing: 0em;
  text-align: left;
  color: white;

  border: none;
  outline: none;
}

.message-input-report {
  height: 50px;
}

.sender_container input,
label {
  width: 100%;
}

.report_container input,
label {
  width: 100%;

}

.sender_container,
.report_container {
  margin-top: 15px;
}

.modal-footer {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: center;
  justify-content: right;
}

.modal_default_button {
  width: 224px;
  height: 50px;
  top: 701px;
  left: 942px;
  border-radius: 15px;
  background: #222222;
  color: #FFFFFF;

  border: none;
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.31em;
  text-align: center;
}

.modal_send_button {
  width: 224px;
  height: 50px;
  top: 701px;
  left: 1176px;
  border-radius: 15px;
  font-family: Montserrat;
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.31em;
  text-align: center;

}
</style>
