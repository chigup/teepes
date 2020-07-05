import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class feed extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[Flexible(child: social_card())],
    );
  }
}

class social_card extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var device_size = MediaQuery
        .of(context)
        .size;
    // TODO: implement build
    return ListView.builder(
        itemCount: 5,
        itemBuilder: (context, index) =>
        index == 0
            ? new SizedBox(
            height: device_size.height * 0.15,
            child: new Text("Write something here...",
                style: TextStyle(
                    color: Colors.grey,
                    fontSize: 20,
                    backgroundColor: Colors.blue)))
            : new card());
  }
}

var src = "data:image/jpeg;base64,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";

class card extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        // 1st row
        Padding(
          padding: EdgeInsets.fromLTRB(15, 15, 15, 15),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              new Container(
                  height: 40.0,
                  width: 40.0,
                  decoration: new BoxDecoration(
                      shape: BoxShape.circle,
                      image: new DecorationImage(
                          fit: BoxFit.fill,
                          image: new NetworkImage(
                              src)))),
              Padding(
                padding: EdgeInsets.fromLTRB(5, 0, 0, 0),
                child: new Text("My name is",
                    style: TextStyle(
                        color: Colors.black, fontWeight: FontWeight.bold)),
              ),
              new IconButton(
                icon: Icon(Icons.more_horiz, color: Colors.black),
                onPressed: () {},
              )
            ],
          ),
        ),
        // 2st row
        Flexible(
          fit: FlexFit.loose,
          child: new Image.network(src, fit: BoxFit.cover),

        ),
        // 3st row
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: new Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                new Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                      new IconButton(
                        icon: Icon(FontAwesomeIcons.heart),
                        onPressed: () {},
                      ),
                      new IconButton(
                        icon: Icon(FontAwesomeIcons.heart),
                        onPressed: () {},
                      ),
                      new IconButton(
                        icon: Icon(FontAwesomeIcons.paperPlane),
                        onPressed: () {},
                      ),
                    ]
                ),
                new IconButton(
                  icon: Icon(FontAwesomeIcons.share),
                  onPressed: () {},
                ),
              ]
          ),

        ),
        // 4st row
        new Row(
            children: <Widget>[
              Expanded(child: new TextField(
                decoration: new InputDecoration(
                  border:InputBorder.none,
                  hintText: "Write to comment",
                )
              ))
            ]
        )
      ],
    );
  }
}
