import { useEffect, useState } from "react";
import { Modal, Table, Tag } from "antd";
import { Client, type Message } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { airportsMap, type Airport } from "~/constant";
import { lazy, Suspense } from "react";

interface Luggage {
  tag: string;
  status: string;
  lastUpdate: string;
  flightNumber: string;
  location: string;
}

const statusColors: Record<string, string> = {
  CHECKED_IN: "blue",
  CLAIMED: "green",
  LOADED: "orange",
  DELIVERED: "orange",
  TRANSFERRED: "orange",
  UNLOADED: "orange",
  ARRIVED: "orange",
};

const MapItem = lazy(() => import("../map"));

const INFO_URL = import.meta.env.REACT_APP_API_INFO_URL
  ? import.meta.env.REACT_APP_API_INFO_URL
  : "http://localhost:8090/api/v1/baggage";
const SOCKET_URL = import.meta.env.REACT_APP_API_SOCKET_URL
  ? import.meta.env.REACT_APP_API_SOCKET_URL
  : "http://localhost:8090/ws/luggage";

export default function LuggageTracker() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalContent, setModalContent] = useState<React.ReactNode | null>(
    null
  );
  const [selectedLocation, setSelectedLocation] = useState<
    Airport | undefined
  >();
  const [luggageList, setLuggageList] = useState<Luggage[]>([]);
  const [highlightedRow, setHighlightedRow] = useState<string | null>(null);

  const stompClient = new Client({
    webSocketFactory: () => new SockJS(SOCKET_URL),
    reconnectDelay: 5000,
    debug: (msg) => console.log(msg),
  });

  useEffect(() => {
    fetch(INFO_URL, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data: Luggage[]) => {
        setLuggageList(data);
      })
      .catch((error) => {
        Modal.error({
          title: "Error while getting data!",
          content:
            "Error occurred while getting luggage data! Reason = " + error,
        });
      });

    stompClient.onConnect = () => {
      console.log("STOMP connected");

      stompClient.subscribe("/topic/luggage-updates", (message: Message) => {
        if (message.body) {
          try {
            const data: Luggage = JSON.parse(message.body);

            setLuggageList((prevList) => {
              const index = prevList.findIndex((item) => item.tag === data.tag);
              if (index !== -1) {
                const newList = [...prevList];
                newList[index] = data;
                setHighlightedRow(prevList[index].tag);
                setTimeout(() => setHighlightedRow(null), 2000);
                return newList;
              } else {
                return [...prevList, data];
              }
            });
          } catch (err) {
            console.error("Error parsing STOMP message:", err);
          }
        }
      });
    };

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, []);

  const columns = [
    {
      title: "Luggage Tag",
      dataIndex: "tag",
      key: "tag",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status: string) => (
        <Tag color={statusColors[status] || "default"}>{status}</Tag>
      ),
    },
    {
      title: "Last Update",
      dataIndex: "lastUpdate",
      key: "lastUpdate",
      render: (date: string) =>
        date === null ? "-" : new Date(date).toLocaleString(),
    },
    {
      title: "Flight",
      dataIndex: "flightNumber",
      key: "flightNumber",
    },
    {
      title: "Location",
      dataIndex: "location",
      key: "location",
      render: (location: string) => (
        <Tag
          color="black"
          onClick={() => {
            setIsModalOpen(true);
            const lctn = airportsMap.get(location);
            const content = getMapContent(lctn);
            setModalContent(content);
            setSelectedLocation(lctn);
          }}
        >
          {location}
        </Tag>
      ),
    },
  ];

  function getMapContent(location: Airport | undefined) {
    return (
      <>
        {location ? (
          <Suspense fallback={<div>Loading Map...</div>}>
            <MapItem
              position={location ? location.position : [1, 1]}
              title={location ? location.name : ""}
            />
          </Suspense>
        ) : (
          <></>
        )}
      </>
    );
  }

  function handleCloseModal() {
    setIsModalOpen(false);
    setModalContent(null);
  }

  return (
    <>
      <div style={{ padding: 24, minHeight: "100vh" }}>
        <div className="mb-4 flex items-center justify-center gap-5">
          <h2 className="font-bold text-zinc-200">
            🛄 Denizhan Airlines - Live Luggage Tracking System
          </h2>
        </div>

        <Table
          rowKey="tag"
          columns={columns}
          dataSource={luggageList}
          rowClassName={(luggage) =>
            luggage.tag === highlightedRow ? "highlight-row" : ""
          }
          pagination={false}
          bordered
        />
      </div>

      {modalContent && (
        <Modal
          title={selectedLocation?.name}
          open={isModalOpen}
          onOk={handleCloseModal}
          onCancel={handleCloseModal}
        >
          {modalContent}
        </Modal>
      )}
    </>
  );
}
