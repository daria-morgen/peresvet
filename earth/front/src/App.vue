<template>
  <div class="reports_list">
    <div class="chat-container">
      <div class="chat-header">
        <h2>"Зенит" (Земля)</h2>
      </div>
      <div class="chat-messages" id="chat-messages">
        <div v-for="(report, index) in reports" :key="index" class="message-container">
          <div class="message-content">
            <!--            <div class="circle"></div> &lt;!&ndash; Кружок &ndash;&gt;-->
            <div class="user-details">
              <span class="author">От: {{ report.authorName }}</span>
              <span class="timestamp">23.02.2020 14:45:31</span>
            </div>
            <div class="chat-message" :class="{ 'expanded': report.expanded }">
              {{ report.expanded ? report.reportText : truncatedText(report.reportText) }}
            </div>
          </div>
          <button :disabled="!report.expanded && (report.reportText.length <= 70)" @click="toggleMessage(report)"
                  class="expand-button">Раскрыть
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      reports: []
    };
  },
  methods: {
    getReportList() {
      this.axios.get('http://localhost:8080/api/reports').then((response) => (this.reports = response.data));
    },
    toggleMessage(report) {
      // Прежде чем изменить значение, проверьте, есть ли текст в отчете
      if (report.reportText && report.reportText.length > 0) {
        report.expanded = !report.expanded;
      }
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
}

.chat-container {
  /*--container-bg-color: #f0f0f0;*/
  max-width: 900px;
  margin: 50px auto;
  border: 1px solid #ccc;

  border-radius: 10px; /* Скругленные углы */
  overflow: hidden;
  box-shadow: 15px 15px 20px rgba(0, 0, 0, 0.5);
  padding: 10px;
  background-size: cover; /* Позволяет изображению занимать всю доступную область */

  /*background: url('./assets/background1.jpg') center center no-repeat;*/
  background-size: cover;
  /* Прозрачность фона */
  /*opacity: 0.5;*/
  /*background-color: var(--container-bg-color);*/
}

.chat-header {
  /*background-color: #ab64db;*/
  /*color: #0d2887;*/
  padding: 10px;
  text-align: center;
  /*color: #f2f2f2;*/

}

.chat-messages {
  max-height: 500px; /* Задаем максимальную высоту для чата */
  overflow-y: auto;
  overflow-x: hidden;
}

.message-container {
  display: flex;
  align-items: center;
  flex-direction: column; /* Изменяем направление флекс-контейнера на строку */
  text-align: left; /* Выравниваем текст слева */
  position: relative;
  margin-bottom: 10px;
  padding-top: 5px;
  margin-right: 10px;
  background-color: #f2f2f2;
  border-radius: 10px;
  box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.5);
}

.user-details {
  display: flex;
  flex-direction: column;
  margin-right: 10px; /* Добавляем отступ справа */
}

.user-details .author {
  font-weight: bold;
}

.user-details .timestamp {
  color: #555;
}

.message-content {
  display: flex;
  align-items: center;
  flex-direction: row;
  padding-left: 10px; /* Изменяем направление флекс-контейнера на колонку */
  /*padding-right: 10px;!* Изменяем направление флекс-контейнера на колонку *!*/
  width: 100%;
}

.chat-message,
.chat-message.expanded {
  display: flex;
  flex-wrap: wrap; /* Позволяет элементам переноситься на следующую строку */
  justify-content: flex-start; /* Размещаем содержимое слева */
  width: 100%;
}

.chat-message {
  /*background-color: #f2f2f2;*/
  padding: 10px;
  border-radius: 5px;
  overflow: hidden;
  margin-bottom: 10px;
  word-wrap: break-word; /* Добавляем перенос слов */

  word-break: break-all; /* Обеспечиваем перенос длинных слов */

}

.chat-message.expanded {
  white-space: normal;
  text-overflow: initial;
}

.expand-button {
  background-color: var(--container-bg-color);
  color: #0a1a68;
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  border-radius: 3px;
  /*margin-top: 5px; !* Добавляем отступ сверху *!*/
  box-shadow: none;
  border: none;
  /*margin-left: auto;*/
}

.expand-button:disabled {
  color: #7e8cd4;
}

.chat-messages::-webkit-scrollbar {
  width: 10px; /* Увеличиваем ширину скроллбара */
}

.chat-messages::-webkit-scrollbar-thumb {
  background-color: #ccc;
  outline: 1px solid #ccc;
  margin-left: 10px; /* Добавляем отступ слева */
}

.chat-messages::-webkit-scrollbar-track {
  background: transparent;
}

/* Скрытие горизонтального скролла */
.chat-messages::-webkit-scrollbar-horizontal {
  display: none;
}

</style>
