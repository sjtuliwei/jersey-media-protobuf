package com.williamli.json.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonProvider implements MessageBodyReader<Message>, MessageBodyWriter<Message> {
	
	public static String LS = System.getProperty("line.separator");
	private ExtensionRegistry extensionRegistry = ExtensionRegistry
			.newInstance();

	@Override
	public boolean isWriteable(Class arg0, Type arg1, Annotation[] arg2, MediaType mediaType) {
		return mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

	@Override
	public long getSize(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Message t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		JsonFormat jsonFormat = new JsonFormat();
		entityStream.write(jsonFormat.printToString(t).getBytes());
	}

	@Override
	public boolean isReadable(Class arg0, Type arg1, Annotation[] arg2, MediaType mediaType) {
		return mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

	@Override
	public Message readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		try {
			Method newBuilder = type.getMethod("newBuilder");

			GeneratedMessage.Builder builder = (GeneratedMessage.Builder) newBuilder.invoke(type);
			String data = convertInputStreamToString(entityStream);
			JsonFormat jsonFormat = new JsonFormat();
			jsonFormat.merge(data, extensionRegistry, builder);
			return (Message) builder.build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	private String convertInputStreamToString(InputStream io) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(io));
			String line = reader.readLine();
			while (line != null) {
				sb.append(line).append(LS);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to obtain an InputStream", e);

		}
		return sb.toString();
	}
}
