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
            Console.WriteLine("test");
            Console.WriteLine(dataType);
            
            //await actions.AddAsync(WebPubSubAction.CreateSendToAllAction(request.Data, dataType));
            await actions.AddAsync(WebPubSubAction.CreateSendToGroupAction("testGroup", request.Data, dataType));
        }
    }
}
