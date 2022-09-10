import Message from './Message';
import React from 'react';
import axios from 'axios';

export default class Chat extends React.Component {
  state = {
    messages:[]
  }

  componentDidMount() {
    axios.get(`http://localhost:8888/messages`).then(response => {
      const messages  = response.data.data[0]
      console.log(messages)
      this.setState({ messages });
    })
  }

  render(){
    return (
      <div>
        <ul role="list" className="divide-y divide-gray-200 my-4 mx-8">
          {this.state.messages.map(({ id, ...activityItem }) => (
            <Message key={id} {...activityItem} />
          ))}
        </ul>
      </div>
    );
  } 
}
