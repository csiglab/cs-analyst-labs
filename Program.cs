using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using OpenTelemetry;
using OpenTelemetry.Metrics;
using OpenTelemetry.Trace;
using OpenTelemetry.Exporter.Zipkin;
using OpenTelemetry.Exporter;
using Microsoft.Extensions.Logging;
using Serilog;
using Serilog.Sinks.Network;
using Serilog.Sinks.Network.Sinks.TCP;
using Serilog.Sinks.Elasticsearch;
using Serilog.Formatting.Compact;

var builder = WebApplication.CreateBuilder(args);

using var meterProvider = Sdk.CreateMeterProviderBuilder()
    .AddProcessInstrumentation()
    // .AddConsoleExporter()
    .Build();

// Add services to the container.
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder
.Services
.AddOpenTelemetry()
.WithTracing(builder => builder
        .AddAspNetCoreInstrumentation()
        // .AddConsoleExporter()
        .AddJaegerExporter(options =>
                        {
                            options.AgentHost = "localhost"; // Replace with your Jaeger host
                            options.AgentPort = 6831; // Replace with your Jaeger port
                        })
        //  .AddOtlpExporter(opt =>
        // {
        //     opt.Endpoint = new Uri("http://localhost:14250/");
        //     opt.Protocol = OtlpExportProtocol.HttpProtobuf;
        // })
        // .AddZipkinExporter(options =>
        //                 {
        //                     options.Endpoint = new Uri("http://localhost:9411/api/v2/spans");
        //                 })
        );


// Configure Logging with OpenTelemetry Integration
builder.Services.AddLogging(builder =>
{
    builder.AddConsole(); // Add any other logging providers you need.
    builder.AddOpenTelemetry(options =>
    {
        options.IncludeScopes = true;
    });
});

// Configure Serilog for logging
Log.Logger = new LoggerConfiguration()
    .WriteTo.Console(new CompactJsonFormatter())
    .WriteTo.Elasticsearch(new ElasticsearchSinkOptions(new Uri("http://localhost:9200"))
    {
        AutoRegisterTemplate = true,
    })
    .WriteTo.OpenTelemetry()
    .CreateLogger();


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();

Log.CloseAndFlush();