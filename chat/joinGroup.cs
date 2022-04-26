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
using Newtonsoft.Json.Linq;

namespace Company.Function
{
    public static class joinGroup
    {
        [FunctionName("joinGroup")]
        public static async Task Run(
            [WebPubSubTrigger("simplechat", WebPubSubEventType.User, "joinGroup")] UserEventRequest request,
            BinaryData data, WebPubSubDataType dataType,
            [WebPubSub(Hub = "simplechat")] IAsyncCollector<WebPubSubAction> actions)
        {
            Console.WriteLine("test");
            Console.WriteLine(dataType);
            string json = data.ToString();
            Console.WriteLine(json);

            

            dynamic json2 = JObject.Parse(json);
            Console.WriteLine(json2.username);
            string username = json2.username;
            string group = json2.group;
            Console.WriteLine(username);
            Console.WriteLine(group);
            /*if ((json2.username is string) && (json2.group is string))
            {
                username = json2.username;
                group = json2.group;
                Console.WriteLine("Adding to grourp");*/
                await actions.AddAsync(WebPubSubAction.CreateAddUserToGroupAction(username, group));
            /*} 
            else
            {
                Console.WriteLine("did not add to grourp");
            }*/

        }
    }
}
