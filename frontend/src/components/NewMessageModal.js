import { Transition } from "@headlessui/react";
import { useState, useEffect } from "react";
import axios from "axios";

export default function NewMessageModal({ messageModalVisible, postedMessage }) {
  const [isMessageModalVisible, setMessageModalVisibility] = useState(false);
  const [message, setMessage] = useState(null);

  useEffect(() => {
    setMessageModalVisibility(messageModalVisible);
  }, [messageModalVisible]);

  function handleClick() {
    axios
      .post("http://localhost:8888/messages", message, {
        headers: {
          "content-type": "text/plain",
        },
      }).then(
        postedMessage()
      )
  }

  return (
    <Transition.Root
      show={messageModalVisible}
      style={{
        width: "100%",
        height: "100%",
        position: "absolute",
        zIndex: 10,
        background: "rgba(0,0,0,0.5)",
      }}
    >
      <div className="flex relative p-4 w-full max-w-2xl bg-white rounded-lg center">
        <textarea
          className="grow"
          style={{ resize: "none" }}
          onChange={(event) => setMessage(event.target.value)}
        />
        <button
          className="flex-none m-8 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
          onClick={() => {
            handleClick();
          }}
        >
          Send
        </button>
      </div>
    </Transition.Root>
  );
}
