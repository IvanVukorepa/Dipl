using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Microsoft.Azure.WebJobs.Extensions.WebPubSub;
using Microsoft.Azure.WebPubSub.Common;
using System.Text.RegularExpressions;
using System.Collections.Generic;

namespace Company.Function
{
    public static class test
    {
        [FunctionName("test")]
        public static async Task Run(
            [WebPubSubTrigger("simplechat", WebPubSubEventType.User, "testevent")] UserEventRequest request,
            BinaryData data, WebPubSubDataType dataType,
            [WebPubSub(Hub = "simplechat")] IAsyncCollector<WebPubSubAction> actions)
        {
            Console.WriteLine("testevent");
            String receivedData = request.Data.ToString();
            String[] receivedDataArr = receivedData.Split('[',']');

            Console.WriteLine(receivedData);
            if (receivedDataArr.Length < 3 )
            {
                //return some error
                return;
            }
            String group = receivedDataArr[1];
            String message = receivedDataArr[2];

            Console.WriteLine(group);

            //await actions.AddAsync(WebPubSubAction.CreateSendToAllAction(request.Data, dataType));
            await actions.AddAsync(WebPubSubAction.CreateSendToGroupAction(group, message, dataType));
        }
    }
}
